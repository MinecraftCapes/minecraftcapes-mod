package net.minecraftcapes.player;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.awt.image.BufferedImage;
import net.minecraftcapes.helpers.ImageHandler;
import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;
import org.apache.commons.io.IOUtils;

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
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static net.minecraftcapes.MinecraftCapes.MINECRAFT_VERSION;

public class DownloadManager {
    private static final Gson GSON = new Gson();
    // Allow short legacy/offline names, but never display names or URL characters.
    private static final Pattern VALID_USERNAME = Pattern.compile("[A-Za-z0-9_]{1,16}");
    private static final ExecutorService DOWNLOAD_EXECUTOR = Executors.newFixedThreadPool(4, runnable -> {
        Thread thread = new Thread(runnable, "MinecraftCapes-Download");
        thread.setDaemon(true);
        return thread;
    });

    /**
     * Prepares a player download, optionally forcing a refresh.
     * @param uuid The profile UUID
     * @param username The player name
     * @param doRefresh Whether to request the profile again
     */
    public static void prepareDownload(UUID uuid, String username, boolean doRefresh) {
        PlayerHandler playerHandler = PlayerHandler.get(uuid);
        synchronized (playerHandler) {
            if (!playerHandler.getHasInfo() || doRefresh) {
                playerHandler.setName(username);
                playerHandler.setHasInfo(true);
                downloadProfile(playerHandler);
            }
        }
    }

    /**
     * Prepares the download
     * @param playerHandler The player handler instance
     */
    public static void prepareDownload(PlayerHandler playerHandler) {
        synchronized (playerHandler) {
            if (!playerHandler.getHasInfo()) {
                playerHandler.setHasInfo(true);
                downloadProfile(playerHandler);
            }
        }
    }

    /**
     * Downloads a profile for a specific PlayerHandler.
     * @param playerHandler
     */
    private static void downloadProfile(PlayerHandler playerHandler) {
        // Capture the identity before queuing: partial profiles may not have a UUID yet.
        UUID uuid = playerHandler.getUuid();
        String username = playerHandler.getName();
        String identifier;
        if (uuid != null && uuid.version() == 4) {
            identifier = uuid.toString().replace("-", "");
        } else if (username != null && VALID_USERNAME.matcher(username).matches()) {
            identifier = username;
        } else {
            // This handler has been attempted; skip invalid or missing names without retrying.
            return;
        }

        DOWNLOAD_EXECUTOR.execute(() -> {
            String downloadUrl = "https://api.minecraftcapes.net/profile/" + identifier;
            byte[] playerDataBytes = downloadData(downloadUrl);
            if (playerDataBytes == null) return;

            String json = new String(playerDataBytes, StandardCharsets.UTF_8);
            ProfileResult profileResult;
            try {
                profileResult = GSON.fromJson(json, ProfileResult.class);
            } catch (JsonParseException e) {
                MinecraftCapes.getLogger().warn("Invalid profile response for {}", identifier, e);
                return;
            }
            if (profileResult == null) return;

            playerHandler.setHasCapeGlint(profileResult.capeGlint);
            playerHandler.setUpsideDown(profileResult.upsideDown);

            // Download cape image if available
            if (profileResult.cape_url != null) {
                BufferedImage capeImage = downloadOrLoad(profileResult.cape_url, "capes");
                if (capeImage != null) {
                    playerHandler.applyCape(capeImage);
                } else {
                    playerHandler.removeCape();
                }
            } else {
                playerHandler.removeCape();
            }

            // Download ears image if available
            if (profileResult.ear_url != null) {
                BufferedImage earsImage = downloadOrLoad(profileResult.ear_url, "ears");
                if (earsImage != null) {
                    playerHandler.applyEars(earsImage);
                } else {
                    playerHandler.removeEars();
                }
            } else {
                playerHandler.removeEars();
            }
        });

    }

    /**
     * Try load the texture from cache or download it
     * @param url
     * @param type
     * @return
     */
    private static BufferedImage downloadOrLoad(String url, String type) {
        String hash = url.substring(url.lastIndexOf('/') + 1);
        Path cache = MinecraftCapes.getConfigDir().resolve(type).resolve(hash.length() > 2 ? hash.substring(0, 2) : "xx").resolve(hash);

        if (Files.isRegularFile(cache)) {
            try (InputStream inputStream = Files.newInputStream(cache)) {
                return ImageHandler.legacyTransparencyFix(inputStream);
            } catch (IOException e) {
                MinecraftCapes.getLogger().warn("Couldn't read cached texture {}; downloading again", cache, e);
            }
        }

        byte[] imageBytes = downloadData(url);
        if (imageBytes == null) return null;

        BufferedImage bufferedImage;
        try (InputStream inputStream = new ByteArrayInputStream(imageBytes)) {
            bufferedImage = ImageHandler.legacyTransparencyFix(inputStream);
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

        return bufferedImage;
    }

    /**
     * Downloads the data for the profile
     * @param url
     * @return
     */
    private static byte[] downloadData(String url) {
        HttpURLConnection conn = null;

        try {
            URI uri = URI.create(url);
            MinecraftCapes.getLogger().debug("Downloading {}", url);
            conn = (HttpURLConnection) uri.toURL().openConnection(Minecraft.getMinecraft().getProxy());
            conn.setRequestProperty("User-Agent", "minecraftcapes-mod/" + MINECRAFT_VERSION);
            conn.setConnectTimeout(10_000);
            conn.setReadTimeout(10_000);

            int status = conn.getResponseCode();
            if (status / 100 == 2) {
                try (InputStream inputStream = conn.getInputStream()) {
                    return IOUtils.toByteArray(inputStream); // Read fully before closing
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
