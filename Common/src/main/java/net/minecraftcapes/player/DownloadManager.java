package net.minecraftcapes.player;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftcapes.MinecraftCapesConstants;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.helpers.MinecraftApi;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

public class DownloadManager {
    
    /**
     * Prepares the down
     * @param player
     */
    public static void prepareDownload(Player player, boolean doRefresh) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;

        //Make sure player is online and not the local player in offline mode
        if(player.getUUID().version() != 4 && (localPlayer != null && !localPlayer.getUUID().equals(player.getUUID()))) return;
    
        PlayerHandler playerHandler = PlayerHandler.getFromPlayer(player);
        //Lets get the local players offline cape
        if(player.getUUID().version() != 4 && !playerHandler.getHasInfo() && !doRefresh) {
            //Stop any more processing
            playerHandler.setHasInfo(true);
            
            //Get UUID from API off main thread
            Thread playerDownload = new Thread(() -> {
                UUID uuid = MinecraftApi.getUUID(player.getScoreboardName());
                if(uuid == null) return;
                playerHandler.setPlayerUUID(uuid);
                
                //Download!
                DownloadManager.downloadProfile(playerHandler);
            });
            playerDownload.setDaemon(true);
            playerDownload.start();
        } else {
            //Make sure we don't have stuff already
            if(playerHandler.getHasInfo() && !doRefresh) return;
            
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
    
            //We've done our processing
            playerHandler.setHasInfo(true);
    
            try {
                MinecraftCapesConstants.LOG.debug("Getting profile for {}", playerHandler.getPlayerUUID());
                URL url = new URL("https://minecraftcapes.net/profile/" + playerHandler.getPlayerUUID().toString().replace("-", ""));
                HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection(Minecraft.getInstance().getProxy());
                httpurlconnection.setDoInput(true);
                httpurlconnection.setDoOutput(false);
                httpurlconnection.connect();

                if (httpurlconnection.getResponseCode() / 100 == 2) {
                    Reader reader = new InputStreamReader(httpurlconnection.getInputStream(), StandardCharsets.UTF_8);
                    DownloadManager.readProfile(playerHandler, reader);
                    reader.close();
                } else {
                    DownloadManager.loadOfflineProfile(playerHandler);
                    MinecraftCapesConstants.LOG.warn("minecraftcapes.net returned a {}", httpurlconnection.getResponseCode());
                }
            } catch (IOException e) {
                DownloadManager.loadOfflineProfile(playerHandler);
                MinecraftCapesConstants.LOG.warn("No connection to minecraftcapes.net detected");
            }
        });
        
        playerDownload.setDaemon(true);
        playerDownload.start();
    }
    
    /**
     * Reads the profile and makes it happen
     * @param playerHandler
     * @param reader
     */
    private static void readProfile(PlayerHandler playerHandler, Reader reader) {
        ProfileResult profileResult = new Gson().fromJson(reader, ProfileResult.class);
        
        playerHandler.setHasCapeGlint(profileResult.capeGlint);
        playerHandler.setUpsideDown(profileResult.upsideDown);
    
        if (profileResult.textures.get("cape") != null) {
            playerHandler.applyCape(profileResult.textures.get("cape"));
        }
    
        if (profileResult.textures.get("ears") != null) {
            playerHandler.applyEars(profileResult.textures.get("ears"));
        }
    
        DownloadManager.cacheProfile(playerHandler, profileResult);
    }
    
    /**
     * Save the profile
     */
    private static void cacheProfile(PlayerHandler playerHandler, ProfileResult profileResult) {
        //Get the UUID has a string
        String fileName = playerHandler.getPlayerUUID().toString();
    
        String profileJson = new Gson().toJson(profileResult);
        
        //Get the profile cache directory and file
        File profileDirectory = new File(MinecraftCapesConfig.getModDirectory() + "/profile");
        File profileFile = new File(new File(profileDirectory, fileName.length() > 2 ? fileName.substring(0, 2) : "xx"), fileName);
    
        //Write the cache file
        try {
            FileUtils.writeStringToFile(profileFile, profileJson, StandardCharsets.UTF_8);
        } catch(Exception e) {
            MinecraftCapesConstants.LOG.error("Error writing cache file");
            e.printStackTrace();
        }
    }
    
    /**
     * Load the offline profile from a cached file
     */
    private static void loadOfflineProfile(PlayerHandler playerHandler) {
        //Get the UUID has a string
        String fileName = playerHandler.getPlayerUUID().toString();
        
        //Get the profile cache directory and file
        File profileDirectory = new File(MinecraftCapesConfig.getModDirectory() + "/profile");
        File profileFile = new File(new File(profileDirectory, fileName.length() > 2 ? fileName.substring(0, 2) : "xx"), fileName);
        
        if(profileFile.exists()) {
            try {
                Reader reader = new FileReader(profileFile);
                DownloadManager.readProfile(playerHandler, reader);
                reader.close();
            } catch(Exception e) {
                MinecraftCapesConstants.LOG.error("Cache corrupt for {}", fileName);
                profileFile.delete();
            }
        }
    }
    
    private static class ProfileResult {
        private boolean capeGlint = false;
        private boolean upsideDown = false;
        private Map<String, String> textures = null;
    }
}
