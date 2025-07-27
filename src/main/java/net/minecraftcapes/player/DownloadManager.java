package net.minecraftcapes.player;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftcapes.MinecraftCapes;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
     * @param playerUUID The players uuid
     * @param doRefresh Whether we are forcing an overwrite
     */
    public static void prepareDownload(UUID playerUUID, boolean doRefresh) {
        EntityPlayerSP localPlayer = Minecraft.getMinecraft().thePlayer;

        //Make sure player is online and not the local player in offline mode
        if(playerUUID.version() != 4 && (localPlayer != null && !localPlayer.getUniqueID().equals(playerUUID))) return;

        // Prep player handler
        PlayerHandler playerHandler = PlayerHandler.get(playerUUID);

        //Lets get the local players offline cape
        if(!playerHandler.getHasInfo() || doRefresh){
            //Download!
            DownloadManager.downloadProfile(playerHandler);
        }
    }

    /**
     * Downloads a profile for a specific PlayerHandler.
     * @param playerHandler
     */
    private static void downloadProfile(PlayerHandler playerHandler) {
        Thread playerDownload = new Thread(() -> {
            playerHandler.setHasInfo(true);

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
                        BufferedImage capeImage = ImageIO.read(new ByteArrayInputStream(capeBytes));
                        playerHandler.applyCape(capeImage);
                    }
                }

                // Download ears image if available
                if (profileResult.ear_url != null) {
                    byte[] earsBytes = downloadData(profileResult.ear_url);
                    if (earsBytes != null) {
                        BufferedImage earsImage = ImageIO.read(new ByteArrayInputStream(earsBytes));
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