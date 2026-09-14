package net.minecraftcapes;

import lombok.Getter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.options.KeyBinding;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.gui.MenuScreen;
import net.ornithemc.osl.keybinds.api.KeybindEvents;
import net.ornithemc.osl.keybinds.api.KeybindRegistry;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.nio.file.Path;

public class MinecraftCapes implements ClientModInitializer {

	public static final String MOD_ID = "minecraftcapes";
	public static final String MOD_NAME = "MinecraftCapes";
	public static final String MINECRAFT_VERSION = "1.8.9";

	@Getter private static final Logger logger = LogManager.getLogger(MOD_NAME);
	@Getter private static final Path configDir = FabricLoaderImpl.INSTANCE.getConfigDir().resolve(MOD_ID);

	private static KeyBinding keyBinding;

	@Override
	public void onInitializeClient() {
		getLogger().info("Initialising");

		//Loading Config
		MinecraftCapesConfig.loadConfig();

		//Configure the KeyBind
		KeybindEvents.REGISTER_KEYBINDS.register(() -> {
			keyBinding = KeybindRegistry.register("key.minecraftcapes.gui", Keyboard.KEY_J, "category.minecraftcapes.gui");
		});

		//React to key pressed
		MinecraftClientEvents.TICK_END.register(client -> {
			while(keyBinding.consumeClick()) {
				Minecraft.getInstance().openScreen(new MenuScreen());
			}
		});

		getLogger().info("Initialised");
	}
}
