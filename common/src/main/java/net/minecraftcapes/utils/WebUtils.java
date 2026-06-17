package net.minecraftcapes.utils;

import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;

import static net.minecraftcapes.MinecraftCapes.MINECRAFT_VERSION;

public class WebUtils {
    
    private static final String USER_AGENT = "minecraftcapes-mod/" + MINECRAFT_VERSION;
    
    /**
     * Downloads the data for the profile
     * @param url
     * @return
     */
    public static byte[] get(String url) {
        HttpURLConnection conn = null;
        URI uri = URI.create(url);
        
        try {
            MinecraftCapes.getLogger().info("Getting texture {}", url);
            conn = (HttpURLConnection) uri.toURL().openConnection(Minecraft.getInstance().getProxy());
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.connect();
            
            if (conn.getResponseCode() / 100 == 2) {
                try (InputStream inputStream = conn.getInputStream()) {
                    return IOUtils.toByteArray(inputStream); // Read fully before closing
                }
            } else {
                MinecraftCapes.getLogger().warn("minecraftcapes.net returned a {}", conn.getResponseCode());
                return null;
            }
        } catch (IOException e) {
            MinecraftCapes.getLogger().warn("No connection to minecraftcapes.net detected");
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
