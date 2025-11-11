package net.minecraftcapes;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.nio.file.Path;

@Mod(MinecraftCapes.MOD_ID)
public class MinecraftCapes {

    public static final String MOD_ID = "minecraftcapes";
    public static final String MINECRAFT_VERSION = Minecraft.getInstance().getVersionType();

    @Getter private static final Logger logger = LogManager.getLogger();
    @Getter private static final Path configDir = FMLPaths.CONFIGDIR.get().resolve(MOD_ID);

    public static final KeyBinding menuKey = new KeyBinding("key.minecraftcapes.gui", GLFW.GLFW_KEY_J, "category.minecraftcapes.gui");

	public MinecraftCapes() {
		getLogger().info("[MinecraftCapes] Initialising");

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

		getLogger().info("[MinecraftCapes] Initialised");
	}

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        //Prep the config
        MinecraftCapesConfig.loadConfig();

        //Register the keybinds
        ClientRegistry.registerKeyBinding(menuKey);
    }

}
