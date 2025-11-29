package net.minecraftcapes.player;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.helpers.ImageHandler;
import net.minecraftcapes.helpers.MinecraftApi;
import org.apache.commons.io.IOUtils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static net.minecraftcapes.MinecraftCapes.MINECRAFT_VERSION;

public class DownloadManager {

    /**
     * Prepares the download
     * @param uuid The entity uuid
     * @param username The entity name
     * @param doRefresh Whether we are forcing an overwrite
     */
    public static void prepareDownload(UUID uuid, String username, boolean doRefresh) {
        PlayerHandler playerHandler = PlayerHandler.get(uuid);
        if (!playerHandler.getHasInfo() || doRefresh) {
            playerHandler.setHasInfo(true);
            if (uuid.version() == 4) {
                downloadProfile(playerHandler);
            } else if (uuid.version() == 3) {
                Thread prepareProfile = new Thread(() -> {
                    UUID onlineUUID = MinecraftApi.getUUID(username);
                    if(onlineUUID != null) {
                        playerHandler.setPlayerUUID(onlineUUID);
                        downloadProfile(playerHandler);
                    }
                });
                prepareProfile.start();
            }
        }
    }

    /**
     * Downloads a profile for a specific PlayerHandler.
     * @param playerHandler
     */
    private static void downloadProfile(PlayerHandler playerHandler) {
        Thread playerDownload = new Thread(() -> {
            byte[] playerDataBytes = downloadData("https://api.minecraftcapes.net/profile/" + playerHandler.getPlayerUUID().toString().replace("-", ""));
            if (playerDataBytes == null) return;

            String json = new String(playerDataBytes, StandardCharsets.UTF_8);
            ProfileResult profileResult = new Gson().fromJson(json, ProfileResult.class);

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

        playerDownload.setDaemon(true);
        playerDownload.start();
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

        BufferedImage bufferedImage = null;

        if(cache.toFile().exists()) {
            try(InputStream inputStream = Files.newInputStream(cache.toFile().toPath())) {
                bufferedImage = ImageHandler.legacyTransparencyFix(inputStream);
            } catch (IOException e) {
                MinecraftCapes.getLogger().error("IOException loading from cache {}", cache);
                MinecraftCapes.getLogger().error(e.getMessage());
                if(cache.toFile().delete()) {
                    return downloadOrLoad(url, type);
                } else {
                    return null;
                }
            }
        } else {
            byte[] imageBytes = downloadData(url);
            if (imageBytes != null) {
                try {
                    Files.createDirectories(cache.getParent());
                    Files.write(cache, imageBytes);
                    bufferedImage = ImageHandler.legacyTransparencyFix(new ByteArrayInputStream(imageBytes));
                } catch (IOException e) {
                    MinecraftCapes.getLogger().error("IOException saving cache {}", url);
                    MinecraftCapes.getLogger().error(e.getMessage());
                    return null;
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
        HttpURLConnection httpURLConnection = null;
        URI uri = URI.create(url);

        try {
            MinecraftCapes.getLogger().info("Getting texture {}", url);
            httpURLConnection = (HttpURLConnection) uri.toURL().openConnection(Minecraft.getMinecraft().getProxy());
            httpURLConnection.setRequestProperty("User-Agent", "minecraftcapes-mod/" + MINECRAFT_VERSION);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() / 100 == 2) {
                try (InputStream inputStream = httpURLConnection.getInputStream()) {
                    return IOUtils.toByteArray(inputStream); // Read fully before closing
                }
            } else {
                MinecraftCapes.getLogger().warn("minecraftcapes.net returned a {}", httpURLConnection.getResponseCode());
                return null;
            }
        } catch (IOException e) {
            MinecraftCapes.getLogger().warn("No connection to minecraftcapes.net detected");
            throw new RuntimeException(e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
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