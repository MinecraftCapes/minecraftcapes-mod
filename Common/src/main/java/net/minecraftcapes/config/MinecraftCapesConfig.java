package net.minecraftcapes.config;

import com.google.gson.Gson;
import lombok.Getter;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MinecraftCapesConfig {

    //File locations
    private static File runDirectory = Minecraft.getInstance().gameDirectory;
    @Getter private static File modDirectory = new File(Minecraft.getInstance().gameDirectory + "/config/" + MinecraftCapes.MOD_ID);
    private static Path configFile = Paths.get(runDirectory + "/config/minecraftcapes.json");

    //The Config Instance
    @Getter private static MinecraftCapesConfig.ConfigValues config = null;

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
            //Create mod directory
            modDirectory.mkdir();
            
            if(!configFile.toFile().exists()) {
                InputStream defaultConfigFile = MinecraftCapesConfig.class.getResourceAsStream("/assets/minecraftcapes/config.json");
                Files.copy(defaultConfigFile, configFile);
            }

            Reader reader = new FileReader(configFile.toFile());
            config = new Gson().fromJson(reader, ConfigValues.class);
            reader.close();
        } catch(IOException e) {
            CrashReport crashreport = new CrashReport("Config error", e);
            configFile.toFile().delete();
            Minecraft.crash(crashreport);
            e.printStackTrace();
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