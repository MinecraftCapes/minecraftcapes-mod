package net.minecraftcapes;

import lombok.Getter;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.SharedConstants;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftforge.common.util.Lazy;
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

    public static Lazy<KeyBinding> menuKey;

	public MinecraftCapes() {
        getLogger().info("Initialising");

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);

        getLogger().info("Initialised");
    }

    public void clientSetup(FMLClientSetupEvent event) {
        // Set the version
        MINECRAFT_VERSION = SharedConstants.getCurrentVersion().getName();

        //Prep the config
        MinecraftCapesConfig.loadConfig();

        //Key Mapping
        menuKey = Lazy.of(() -> new KeyBinding(
                "key.minecraftcapes.gui",
                InputMappings.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                "category.minecraftcapes.gui"
        ));

        //Register the keybinds
        ClientRegistry.registerKeyBinding(menuKey.get());
    }

    public void serverSetup(FMLDedicatedServerSetupEvent event) {
        MinecraftCapes.getLogger().error("=============================================");
        MinecraftCapes.getLogger().error("MinecraftCapes only needs to be on the client");
        MinecraftCapes.getLogger().error("     You'll still see each others capes!");
        MinecraftCapes.getLogger().error("     Please remove this from your server!");
        MinecraftCapes.getLogger().error("=============================================");
    }
}
