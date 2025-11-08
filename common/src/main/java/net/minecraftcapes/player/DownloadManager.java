package net.minecraftcapes.player;

import com.google.gson.Gson;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.helpers.MinecraftApi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static net.minecraftcapes.MinecraftCapes.MINECRAFT_VERSION;

public class DownloadManager {

    /**
     * Prepares the down
     * @param uuid The entity uuid
     * @param username The entity name
     * @param doRefresh Whether we are forcing an overwrite
     */
    public static void prepareDownload(UUID uuid, String username, boolean doRefresh) {
        PlayerHandler playerHandler = PlayerHandler.get(uuid);
        if (!playerHandler.getHasInfo() && !doRefresh) {
            playerHandler.setHasInfo(true);
            if (uuid.version() == 4) {
                downloadProfile(playerHandler);
            } else if (uuid.version() == 3) {
                Thread prepareProfile = new Thread(() -> {
                    UUID onlineUUID = MinecraftApi.getUUID(username);
                    playerHandler.setPlayerUUID(onlineUUID);
                    downloadProfile(playerHandler);
                });
                prepareProfile.start();
            }
        }
    }

    public static void prepareDownload(PlayerHandler playerHandler) {
        if(!playerHandler.getHasInfo()) {
            playerHandler.setHasInfo(true);
            downloadProfile(playerHandler);
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

            try {
                String json = new String(playerDataBytes, StandardCharsets.UTF_8);
                ProfileResult profileResult = new Gson().fromJson(json, ProfileResult.class);

                playerHandler.setHasCapeGlint(profileResult.capeGlint);
                playerHandler.setUpsideDown(profileResult.upsideDown);

                // Download cape image if available
                if (profileResult.cape_url != null) {
                    byte[] capeBytes = downloadData(profileResult.cape_url);
                    if (capeBytes != null) {
                        NativeImage capeImage = NativeImage.read(new ByteArrayInputStream(capeBytes));
                        playerHandler.applyCape(capeImage);
                    }
                }

                // Download ears image if available
                if (profileResult.ear_url != null) {
                    byte[] earsBytes = downloadData(profileResult.ear_url);
                    if (earsBytes != null) {
                        NativeImage earsImage = NativeImage.read(new ByteArrayInputStream(earsBytes));
                        playerHandler.applyEars(earsImage);
                    }
                }
            } catch (IOException e) {
                MinecraftCapes.getLogger().warn("Error downloading profile data", e);
            }
        });

        playerDownload.setDaemon(true);
        playerDownload.start();
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
            httpURLConnection = (HttpURLConnection) uri.toURL().openConnection(Minecraft.getInstance().getProxy());
            httpURLConnection.setRequestProperty("User-Agent", "minecraftcapes-mod/" + MINECRAFT_VERSION);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() / 100 == 2) {
                try (InputStream inputStream = httpURLConnection.getInputStream()) {
                    return inputStream.readAllBytes(); // Read fully before closing
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
