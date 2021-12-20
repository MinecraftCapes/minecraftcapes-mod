package net.minecraftcapes.player;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftcapes.MinecraftCapesConstants;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class DownloadManager {
    
    public static void prepareDownload(Player player) {
        //Make sure player is a player and online
        if(player.getUUID().version() != 4) return;
        
        //Make sure we dont have stuff already
        PlayerHandler playerHandler = PlayerHandler.getFromPlayer(player);
        if(playerHandler == null || playerHandler.getHasInfo()) return;
        
        //Download!
        DownloadManager.downloadProfile(playerHandler);
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
                    Reader reader = new InputStreamReader(httpurlconnection.getInputStream(), "UTF-8");
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
    
    class ProfileResult {
        private boolean capeGlint = false;
        private boolean upsideDown = false;
        private Map<String, String> textures = null;
    }
}
