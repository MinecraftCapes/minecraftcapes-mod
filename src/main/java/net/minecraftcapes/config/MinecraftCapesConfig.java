package net.minecraftcapes.config;

import com.google.gson.Gson;
import lombok.Getter;
import net.minecraftcapes.MinecraftCapes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class MinecraftCapesConfig {

    //File locations
    private static final Path configFile = MinecraftCapes.getConfigDir().resolve("minecraftcapes.json");

    //The Config Instance
    @Getter private static ConfigValues config = new ConfigValues();

    /**
     * The config values
     */
    static class ConfigValues {
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
            //Create mod directory
            Files.createDirectories(configFile.getParent());

            if(!configFile.toFile().exists()) {
                saveConfig();
            }

            Reader reader = new FileReader(configFile.toFile());
            config = new Gson().fromJson(reader, ConfigValues.class);
            reader.close();
        } catch(IOException e) {
            if(configFile.toFile().delete()) {
                loadConfig();
            }
        }
    }

    /**
     * Save the config
     */
    private static void saveConfig() {
        try {
            Writer writer = new FileWriter(configFile.toFile());
            new Gson().toJson(config, writer);
            writer.flush();
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}