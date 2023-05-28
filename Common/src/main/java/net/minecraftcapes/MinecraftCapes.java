package net.minecraftcapes;

import lombok.Getter;
import net.minecraftcapes.config.MinecraftCapesConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MinecraftCapes {
    
    public static final String MOD_ID = "minecraftcapes";
    public static final String MOD_NAME = "MinecraftCapes";
    @Getter
    private static final Logger logger = LogManager.getLogger(MOD_NAME);
    
    public static void onEnable() {
        MinecraftCapes.getLogger().info("Initialising");
        
        //Loading Config
        MinecraftCapesConfig.loadConfig();
    }
}
