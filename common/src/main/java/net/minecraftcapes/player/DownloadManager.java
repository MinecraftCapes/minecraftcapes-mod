package net.minecraftcapes.player;

import com.google.gson.Gson;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.helpers.MinecraftApi;
import net.minecraftcapes.utils.WebUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class DownloadManager {
    
    /**
     * Prepares the download
     * @param playerHandler The player handler instance
     */
    public static void prepareDownload(PlayerHandler playerHandler) {
        if (!playerHandler.getHasInfo()) {
            playerHandler.setHasInfo(true);
            if (playerHandler.getUuid().version() == 4) {
                downloadProfile(playerHandler);
            } else if (playerHandler.getUuid().version() == 3) {
                Thread prepareProfile = new Thread(() -> {
                    UUID onlineUUID = MinecraftApi.getUUID(playerHandler.getName());
                    if(onlineUUID != null) {
                        playerHandler.setUuid(onlineUUID);
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
            byte[] playerDataBytes = WebUtils.get("https://api.minecraftcapes.net/profile/" + playerHandler.getUuid().toString().replace("-", ""));
            if (playerDataBytes == null) return;

            String json = new String(playerDataBytes, StandardCharsets.UTF_8);
            ProfileResult profileResult = new Gson().fromJson(json, ProfileResult.class);

            playerHandler.setHasCapeGlint(profileResult.capeGlint);
            playerHandler.setUpsideDown(profileResult.upsideDown);

            // Download cape image if available
            if (profileResult.cape_url != null) {
                NativeImage capeImage = downloadOrLoad(profileResult.cape_url, "capes");
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
                NativeImage earsImage = downloadOrLoad(profileResult.ear_url, "ears");
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
    private static NativeImage downloadOrLoad(String url, String type) {
        String hash = url.substring(url.lastIndexOf('/') + 1);
        Path cache = MinecraftCapes.getConfigDir().resolve(type).resolve(hash.length() > 2 ? hash.substring(0, 2) : "xx").resolve(hash);

        NativeImage nativeImage = null;

        if(cache.toFile().exists()) {
            try(InputStream inputStream = Files.newInputStream(cache.toFile().toPath())) {
                nativeImage = NativeImage.read(inputStream);
            } catch (IOException e) {
                MinecraftCapes.getLogger().error("IOException with {}", cache);
                MinecraftCapes.getLogger().error(e.getMessage());
                if(cache.toFile().delete()) {
                    return downloadOrLoad(url, type);
                } else {
                    return null;
                }
            }
        } else {
            byte[] imageBytes = WebUtils.get(url);
            if (imageBytes != null) {
                try {
                    Files.createDirectories(cache.getParent());
                    Files.write(cache, imageBytes);
                    nativeImage = NativeImage.read(new ByteArrayInputStream(imageBytes));
                } catch (IOException e) {
                    MinecraftCapes.getLogger().error("IOException with {}", url);
                    MinecraftCapes.getLogger().error(e.getMessage());
                    return null;
                }
            }
        }

        return nativeImage;
    }

    private static class ProfileResult {
        private boolean capeGlint = false;
        private boolean upsideDown = false;
        private String cape_url = null;
        private String ear_url = null;
    }
}