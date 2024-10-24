package net.minecraftcapes.helpers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraftcapes.MinecraftCapes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class MinecraftApi {

    /**
     * Returns player UUID
     *
     * @param username Username to get UUID from
     * @return Players uuid
     */
    public static UUID getUUID(String username) {
        MinecraftCapes.getLogger().warn("Making an API call for {}", username);
        JsonObject playerElement = getApiData(username);
        if (playerElement != null) {
            JsonElement playerUUID = playerElement.get("full_uuid");
            if (playerUUID != null && !playerUUID.isJsonNull()) {
                MinecraftCapes.getLogger().warn("{} ({}) was found", username, playerUUID);
                return UUID.fromString(playerUUID.getAsString());
            }
        }
        MinecraftCapes.getLogger().warn("{} was not found", username);
        return null;
    }

    /**
     * Request API call for user data
     *
     * @param data The username/uuid to send
     * @return The response data
     */
    private static JsonObject getApiData(String data) {
        try {
            URL url = new URL("https://api.minecraftapi.net/api/v2/profile/" + data);
            HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
            httpurlconnection.setRequestProperty("User-Agent", "minecraftcapes-mod/1.20.2");
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(false);
            httpurlconnection.connect();

            if (httpurlconnection.getResponseCode() / 100 == 2) {
                //Create reader
                BufferedReader in = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                //Read response
                while ((inputLine = in.readLine()) != null)
                    response.append(inputLine);

                //Convert response to JSON
                return JsonParser.parseString(response.toString()).getAsJsonObject();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}