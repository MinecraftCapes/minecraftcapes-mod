package net.minecraftcapes;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.nio.file.Path;

@Mod(MinecraftCapes.MOD_ID)
public class MinecraftCapes {

    public static final String MOD_ID = "minecraftcapes";
    public static final String MOD_NAME = "MinecraftCapes";
    public static String MINECRAFT_VERSION;

    @Getter private static final Logger logger = LogManager.getLogger(MOD_NAME);
    @Getter private static final Path configDir = FMLPaths.CONFIGDIR.get().resolve(MOD_ID);

    public static KeyBinding menuKey;

	public MinecraftCapes() {
        getLogger().info("[MinecraftCapes] Initialising");

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);

        getLogger().info("[MinecraftCapes] Initialised");
    }

    public void clientSetup(FMLClientSetupEvent event) {
        // Set the version
        MINECRAFT_VERSION = Minecraft.getInstance().getVersionType();

        // Set the key binding
        menuKey = new KeyBinding(
                "Open GUI",
                GLFW.GLFW_KEY_J,
                "MinecraftCapes"
        );

        //Prep the config
        MinecraftCapesConfig.loadConfig();

        //Register the keybinds
        ClientRegistry.registerKeyBinding(menuKey);
    }

    public void serverSetup(FMLDedicatedServerSetupEvent event) {
        MinecraftCapes.getLogger().error("=============================================");
        MinecraftCapes.getLogger().error("MinecraftCapes only needs to be on the client");
        MinecraftCapes.getLogger().error("     You'll still see each others capes!");
        MinecraftCapes.getLogger().error("     Please remove this from your server!");
        MinecraftCapes.getLogger().error("=============================================");
    }
}
