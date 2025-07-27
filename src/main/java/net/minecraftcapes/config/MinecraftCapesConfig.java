package net.minecraftcapes.config;

import com.google.gson.Gson;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class MinecraftCapesConfig {

    //File locations
    private static File runDirectory = Minecraft.getInstance().gameDir;
    private static File configFile = new File(runDirectory, "/config/minecraftcapes.json");

    //The Config Instance
    @Getter private static ConfigValues config = null;

    /**
     * The config values
     */
    class ConfigValues {
        private boolean capeVisible = true;
        private boolean earsVisible = true;
    }

    /**
     * Change cape visibility
     * @param enabled
     */
    public static void setCapeVisible(boolean enabled) {
        config.capeVisible = enabled;
        saveConfig();
    }

    /**
     * Change ears visibility
     * @param enabled
     */
    public static void setEarsVisible(boolean enabled) {
        config.earsVisible = enabled;
        saveConfig();
    }

    /**
     * Cape visible
     * @return boolean
     */
    public static boolean isCapeVisible() {
        return config.capeVisible;
    }

    /**
     * Ears visible
     * @return boolean
     */
    public static boolean isEarsVisible() {
        return config.earsVisible;
    }

    /**
     * Load the config into memory
     */
    public static void loadConfig() {
        try {
            if(!configFile.exists()) {
                InputStream defaultConfigFile = MinecraftCapesConfig.class.getResourceAsStream("/assets/minecraftcapes/config.json");
                FileUtils.copyInputStreamToFile(defaultConfigFile, configFile);
            }

            Reader reader = new FileReader(configFile);
            config = new Gson().fromJson(reader, ConfigValues.class);
            reader.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the config
     */
    private static void saveConfig() {
        try {
            Writer writer = new FileWriter(configFile);
            new Gson().toJson(config, writer);
            writer.flush();
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
