package net.minecraftcapes;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.renderer.RenderType;
import net.minecraftcapes.config.MinecraftCapesConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MinecraftCapes {
    
    public static final String MOD_ID = "minecraftcapes";
    public static final String MOD_NAME = "MinecraftCapes";
    @Getter
    private static final Logger logger = LogManager.getLogger(MOD_NAME);
    @Getter @Setter
    protected static RenderType capeGlint;
    
    public static void onEnable() {
        MinecraftCapes.getLogger().info("Initialising");
        
        //Loading Config
        MinecraftCapesConfig.loadConfig();
    }
    
    public static void onDisable() {
    
    }
}
