package net.minecraftcapes.player;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import static net.minecraftcapes.MinecraftCapes.MINECRAFT_VERSION;

/** Fetches profiles and owns hash-keyed textures for the lifetime of the client. */
public class DownloadManager {
    private static final Gson GSON = new Gson();
    private static final Pattern VALID_USERNAME = Pattern.compile("[A-Za-z0-9_]{1,16}");
    private static final Pattern VALID_HASH = Pattern.compile("[a-f0-9]{64}");
    private static final ExecutorService DOWNLOAD_EXECUTOR = Executors.newFixedThreadPool(4, runnable -> {
        Thread thread = new Thread(runnable, "MinecraftCapes-Download");
        thread.setDaemon(true);
        return thread;
    });
    // Guarded by DownloadManager.class, including the handoff of decoded images to the client thread.
    private static final Map<String, CompletableFuture<SharedTexture>> TEXTURES = new HashMap<>();
    private static final Set<SharedTexture.Prepared> PENDING_UPLOADS = new HashSet<>();
    private static volatile boolean closed;

    /** Starts one profile attempt on the client thread; retries still require a profile refresh. */
    public static void prepareDownload(PlayerHandler handler) {
        if (closed || !handler.beginDownload()) return;
        UUID uuid = handler.getUuid();
        String username = handler.getUsername();
        String identifier;
        if (uuid != null && uuid.version() == 4) {
            identifier = uuid.toString().replace("-", "");
        } else if (username != null && VALID_USERNAME.matcher(username).matches()) {
            identifier = username;
        } else {
            MinecraftCapes.getLogger().debug("Skipping for {} - {}", uuid, username);
            return;
        }
        DOWNLOAD_EXECUTOR.execute(() -> downloadProfile(handler, identifier));
    }

    /** Fetches profile data on a worker and hands all profile-state changes to the client thread. */
    private static void downloadProfile(PlayerHandler handler, String identifier) {
        if (closed) return;
        byte[] bytes = downloadData("https://api.minecraftcapes.net/profile/" + identifier);
        if (bytes == null || closed) return;
        try {
            ProfileResult profile = GSON.fromJson(new String(bytes, StandardCharsets.UTF_8), ProfileResult.class);
            if (profile != null) Minecraft.getInstance().execute(() -> applyProfile(handler, profile));
        } catch (JsonParseException e) {
            MinecraftCapes.getLogger().warn("Invalid profile response for {}", identifier, e);
        }
    }

    /** Requests shared cosmetics without blocking the client thread or tying their lifetime to a player. */
    private static void applyProfile(PlayerHandler handler, ProfileResult profile) {
        if (closed || !handler.isCurrent()) return;
        handler.applyProfile(profile.capeGlint, profile.upsideDown);
        loadTexture(profile.cape_url, "capes").thenAcceptAsync(texture -> {
            if (!closed) handler.applyCape(texture);
        }, Minecraft.getInstance());
        loadTexture(profile.ear_url, "ears").thenAcceptAsync(texture -> {
            if (!closed) handler.applyEars(texture == null ? null : texture.staticLocation());
        }, Minecraft.getInstance());
    }

    /** Shares both in-progress work and completed GPU resources by cosmetic type and SHA-256 hash. */
    private static synchronized CompletableFuture<SharedTexture> loadTexture(String url, String type) {
        if (url == null || closed) return CompletableFuture.completedFuture(null);
        String hash;
        try {
            String path = URI.create(url).getPath();
            hash = path.substring(path.lastIndexOf('/') + 1);
            if (!VALID_HASH.matcher(hash).matches()) throw new IllegalArgumentException("Invalid texture hash");
        } catch (IllegalArgumentException | NullPointerException e) {
            MinecraftCapes.getLogger().warn("Invalid texture URL {}", url, e);
            return CompletableFuture.completedFuture(null);
        }
        String key = type + "/" + hash;
        CompletableFuture<SharedTexture> existing = TEXTURES.get(key);
        if (existing != null) return existing;
        CompletableFuture<SharedTexture> future = new CompletableFuture<>();
        TEXTURES.put(key, future);
        DOWNLOAD_EXECUTOR.execute(() -> prepareTexture(url, type, hash, key, future));
        return future;
    }

    /** Loads and prepares one hash on a worker, tracking native memory until upload or shutdown. */
    private static void prepareTexture(String url, String type, String hash, String key,
                                       CompletableFuture<SharedTexture> future) {
        if (closed) return;
        try {
            NativeImage image = downloadOrLoad(url, type, hash);
            if (image == null) {
                finishFailure(key, future);
                return;
            }
            if (closed) {
                image.close();
                return;
            }
            SharedTexture.Prepared prepared = type.equals("capes")
                    ? SharedTexture.prepareCape(image)
                    : SharedTexture.prepareEars(image);
            synchronized (DownloadManager.class) {
                if (closed) {
                    prepared.close();
                    return;
                }
                PENDING_UPLOADS.add(prepared);
                try {
                    Minecraft.getInstance().execute(() -> registerTexture(key, future, prepared));
                } catch (RuntimeException e) {
                    PENDING_UPLOADS.remove(prepared);
                    prepared.close();
                    throw e;
                }
            }
        } catch (RuntimeException e) {
            MinecraftCapes.getLogger().warn("Couldn't prepare texture {}", key, e);
            finishFailure(key, future);
        }
    }

    /** Publishes a texture only after every frame has been registered on the client thread. */
    private static synchronized void registerTexture(String key, CompletableFuture<SharedTexture> future,
                                                     SharedTexture.Prepared prepared) {
        if (closed) return; // Shutdown already closed all pending images.
        PENDING_UPLOADS.remove(prepared);
        try {
            future.complete(prepared.register(key));
        } catch (RuntimeException e) {
            MinecraftCapes.getLogger().warn("Couldn't register texture {}", key, e);
            finishFailure(key, future);
        }
    }

    /** Allows a later profile request to retry a failed hash without automatically retrying this profile. */
    private static synchronized void finishFailure(String key, CompletableFuture<SharedTexture> future) {
        TEXTURES.remove(key, future);
        future.complete(null);
    }

    /** Closes shared GPU resources before Minecraft destroys its renderer; never called on profile removal. */
    public static synchronized void shutdown() {
        closed = true;
        DOWNLOAD_EXECUTOR.shutdownNow();
        for (SharedTexture.Prepared prepared : PENDING_UPLOADS) prepared.close();
        PENDING_UPLOADS.clear();
        for (CompletableFuture<SharedTexture> future : TEXTURES.values()) {
            SharedTexture texture = future.getNow(null);
            if (texture != null) texture.close();
            future.complete(null);
        }
        TEXTURES.clear();
        PlayerHandler.clearAll();
    }

    /**
     * Try load the texture from cache or download it
     * @param url Remote profile or texture URL.
     * @param type Existing cache subdirectory: capes or ears.
     * @param hash Validated texture hash used as the cache filename.
     */
    private static NativeImage downloadOrLoad(String url, String type, String hash) {
        Path cache = MinecraftCapes.getConfigDir().resolve(type).resolve(hash.substring(0, 2)).resolve(hash);

        if (Files.isRegularFile(cache)) {
            try (InputStream inputStream = Files.newInputStream(cache)) {
                return NativeImage.read(inputStream);
            } catch (IOException e) {
                MinecraftCapes.getLogger().warn("Couldn't read cached texture {}; downloading again", cache, e);
            }
        }

        byte[] imageBytes = downloadData(url);
        if (imageBytes == null) return null;

        NativeImage nativeImage;
        try (InputStream inputStream = new ByteArrayInputStream(imageBytes)) {
            nativeImage = NativeImage.read(inputStream);
        } catch (IOException e) {
            MinecraftCapes.getLogger().warn("Couldn't decode texture {}", url, e);
            return null;
        }

        // Publish only complete, validated files so concurrent readers never see partial data.
        Path temporary = null;
        try {
            Files.createDirectories(cache.getParent());
            temporary = Files.createTempFile(cache.getParent(), "texture-", ".tmp");
            Files.write(temporary, imageBytes);
            try {
                Files.move(temporary, cache, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporary, cache, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            // A read-only or full cache must not prevent displaying a valid texture.
            MinecraftCapes.getLogger().warn("Couldn't cache texture {}", cache, e);
        } finally {
            if (temporary != null) {
                try {
                    Files.deleteIfExists(temporary);
                } catch (IOException e) {
                    MinecraftCapes.getLogger().warn("Couldn't remove temporary texture {}", temporary, e);
                }
            }
        }

        return nativeImage;
    }

    /**
     * Downloads the data for the profile
     * @param url Remote profile or texture URL.
     */
    private static byte[] downloadData(String url) {
        HttpURLConnection conn = null;

        try {
            URI uri = URI.create(url);
            MinecraftCapes.getLogger().debug("Downloading {}", url);
            conn = (HttpURLConnection) uri.toURL().openConnection(Minecraft.getInstance().getProxy());
            conn.setRequestProperty("User-Agent", "minecraftcapes-mod/" + MINECRAFT_VERSION);
            conn.setConnectTimeout(10_000);
            conn.setReadTimeout(10_000);

            int status = conn.getResponseCode();
            if (status / 100 == 2) {
                try (InputStream inputStream = conn.getInputStream()) {
                    return inputStream.readAllBytes();
                }
            } else {
                MinecraftCapes.getLogger().warn("Download {} returned HTTP {}", url, status);
                return null;
            }
        } catch (IOException | IllegalArgumentException e) {
            MinecraftCapes.getLogger().warn("Couldn't download {}", url, e);
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static class ProfileResult {
        private boolean capeGlint = false;
        private boolean upsideDown = false;
        private String cape_url = null;
        private String ear_url = null;
    }
}
