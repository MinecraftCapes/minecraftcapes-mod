package net.minecraftcapes;

import com.mojang.blaze3d.platform.InputConstants;
import lombok.Getter;
import net.minecraft.SharedConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftcapes.config.MinecraftCapesConfig;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class MinecraftCapes {
    
    public static final String MOD_ID = "minecraftcapes";
    public static final String MOD_NAME = "MinecraftCapes";
    public static final String MINECRAFT_VERSION = SharedConstants.getCurrentVersion().name();
    
    @Getter
    private static Path configDir;
    
    @Getter
    private static final Logger logger = LoggerFactory.getLogger(MOD_NAME);
    
    public static final KeyMapping KEY_MAPPING = new KeyMapping(
            "key.minecraftcapes.gui",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            "category.minecraftcapes.gui"
    );
    
    public static void onEnable(Path configDir) {
        MinecraftCapes.getLogger().info("Initialising");
        
        //Loading Config
        MinecraftCapes.configDir = configDir.resolve(MOD_ID);
        MinecraftCapesConfig.loadConfig();
        
        MinecraftCapes.getLogger().info("Initialised");
    }
}