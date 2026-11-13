package net.minecraftcapes.config;

import com.google.gson.Gson;
import lombok.Getter;
import net.minecraftcapes.MinecraftCapes;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class MinecraftCapesConfig {

    private static final Gson GSON = new Gson();

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
     * @param enabled Whether the cosmetic should be visible.
     */
    public static void setCapeVisible(boolean enabled) {
        config.capeVisible = enabled;
        saveConfig();
    }

    /**
     * Change ears visibility
     * @param enabled Whether the cosmetic should be visible.
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

            try (Reader reader = new FileReader(configFile.toFile())) {
                config = GSON.fromJson(reader, ConfigValues.class);
            }
        } catch(Exception e) {
            if(configFile.toFile().delete()) {
                loadConfig();
            }
        }
    }

    /**
     * Save the config
     */
    private static void saveConfig() {
        try (Writer writer = new FileWriter(configFile.toFile())) {
            GSON.toJson(config, writer);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}