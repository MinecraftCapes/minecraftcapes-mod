package net.minecraftcapes.player;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftcapes.MinecraftCapesConstants;
import net.minecraftcapes.helpers.MinecraftApi;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

public class DownloadManager {
    
    public static void prepareDownload(Player player) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;

        //Make sure player is online and not the local player in offline mode
        if(player.getUUID().version() != 4 && (localPlayer != null && !localPlayer.getUUID().equals(player.getUUID()))) return;
    
        PlayerHandler playerHandler = PlayerHandler.getFromPlayer(player);
        //Lets get the local players offline cape
        if(player.getUUID().version() != 4) {
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
            if(playerHandler.getHasInfo()) return;
            
            //Download!
            DownloadManager.downloadProfile(playerHandler);
        }
    }
    
    public static void downloadProfile(PlayerHandler playerHandler) {
        Thread playerDownload = new Thread(() -> {
            try {
                MinecraftCapesConstants.LOG.debug("Getting profile for {}", playerHandler.getPlayerUUID());
                URL url = new URL("https://minecraftcapes.net/profile/" + playerHandler.getPlayerUUID().toString().replace("-", ""));
                HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection(Minecraft.getInstance().getProxy());
                httpurlconnection.setDoInput(true);
                httpurlconnection.setDoOutput(false);
                httpurlconnection.connect();
                
                if (httpurlconnection.getResponseCode() / 100 == 2) {
                    Reader reader = new InputStreamReader(httpurlconnection.getInputStream(), StandardCharsets.UTF_8);
                    ProfileResult profileResult = new Gson().fromJson(reader, ProfileResult.class);
                    reader.close();
                    
                    playerHandler.setHasInfo(true);
                    playerHandler.setHasCapeGlint(profileResult.capeGlint);
                    playerHandler.setUpsideDown(profileResult.upsideDown);
                    
                    if (profileResult.textures.get("cape") != null) {
                        playerHandler.applyCape(profileResult.textures.get("cape"));
                    }
                    
                    if (profileResult.textures.get("ears") != null) {
                        playerHandler.applyEars(profileResult.textures.get("ears"));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        playerDownload.setDaemon(true);
        playerDownload.start();
    }
    
    static class ProfileResult {
        private boolean capeGlint = false;
        private boolean upsideDown = false;
        private Map<String, String> textures = null;
    }
}
