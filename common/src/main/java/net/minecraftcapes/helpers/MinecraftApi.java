package net.minecraftcapes.helpers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static net.minecraftcapes.MinecraftCapes.MINECRAFT_VERSION;

public class MinecraftApi {

    /**
     * Returns player UUID
     *
     * @param username Username to get UUID from
     * @return Players uuid
     */
    public static UUID getUUID(String username) {
        MinecraftCapes.getLogger().debug("Making an API call for {}", username);
        JsonObject playerElement = getApiData(username);
        if (playerElement != null) {
            JsonElement playerUUID = playerElement.get("full_uuid");
            if (playerUUID != null && !playerUUID.isJsonNull()) {
                MinecraftCapes.getLogger().debug("{} ({}) was found", username, playerUUID);
                return UUID.fromString(playerUUID.getAsString());
            }
        }
        MinecraftCapes.getLogger().debug("{} was not found", username);
        return null;
    }

    /**
     * Request API call for user data
     *
     * @param data The username/uuid to send
     * @return The response data
     */
    private static JsonObject getApiData(String data) {
        HttpURLConnection conn = null;
        try {
            URI uri = URI.create("https://api.minecraftapi.net/v3/profile/" + data + "?params=[full_uuid,name]");
            
            conn = (HttpURLConnection) uri.toURL().openConnection(Minecraft.getInstance().getProxy());
            conn.setRequestProperty("User-Agent", "minecraftcapes-mod/" + MINECRAFT_VERSION);
            
            int code = conn.getResponseCode();
            InputStream stream = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
            if (stream == null || code < 200 || code >= 300) return null;
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                char[] buf = new char[2048];
                int n;
                while ((n = reader.read(buf)) != -1) sb.append(buf, 0, n);
                return JsonParser.parseString(sb.toString()).getAsJsonObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

}