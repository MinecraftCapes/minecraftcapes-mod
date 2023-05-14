package net.minecraftcapes.player;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftcapes.MinecraftCapes;
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

    public static void prepareDownload(Player player, boolean doRefresh) {
        prepareDownload(player.getUUID(), player.getGameProfile().getName(), doRefresh);
    }
    public static void prepareDownload(PlayerInfo playerInfo, boolean doRefresh) {
        prepareDownload(playerInfo.getProfile().getId(), playerInfo.getProfile().getName(), doRefresh);
    }

    /**
     * Prepares the down
     * @param playerUUID The players uuid
     * @param playerName The players name
     * @param doRefresh Whether we are forcing an overwrite
     */
    private static void prepareDownload(UUID playerUUID, String playerName, boolean doRefresh) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;

        //Make sure player is online and not the local player in offline mode
        if(playerUUID.version() != 4 && (localPlayer != null && !localPlayer.getUUID().equals(playerUUID))) return;

        PlayerHandler playerHandler = PlayerHandler.get(playerUUID);
        //Lets get the local players offline cape
        if(playerUUID.version() != 4 && !playerHandler.getHasInfo() && !doRefresh) {
            //Stop any more processing
            playerHandler.setHasInfo(true);

            //Get UUID from API off main thread
            Thread playerDownload = new Thread(() -> {
                UUID uuid = MinecraftApi.getUUID(playerName);
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
                MinecraftCapes.getLogger().debug("Getting profile for {}", playerHandler.getPlayerUUID());
                URL url = new URL("https://api.minecraftcapes.net/profile/" + playerHandler.getPlayerUUID().toString().replace("-", ""));
                HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection(Minecraft.getInstance().getProxy());
                httpurlconnection.setDoInput(true);
                httpurlconnection.setDoOutput(false);
                httpurlconnection.connect();

                if (httpurlconnection.getResponseCode() / 100 == 2) {
                    Reader reader = new InputStreamReader(httpurlconnection.getInputStream(), StandardCharsets.UTF_8);
                    DownloadManager.readProfile(playerHandler, reader);
                    reader.close();
                } else {
                    MinecraftCapes.getLogger().warn("minecraftcapes.net returned a {}", httpurlconnection.getResponseCode());
                }
            } catch (IOException e) {
                MinecraftCapes.getLogger().warn("No connection to minecraftcapes.net detected");
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
    }

    private static class ProfileResult {
        private boolean capeGlint = false;
        private boolean upsideDown = false;
        private Map<String, String> textures = null;
    }
}
