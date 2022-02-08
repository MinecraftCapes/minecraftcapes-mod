package net.minecraftcapes;

import lombok.Getter;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.options.KeyBinding;
import net.minecraftcapes.config.MinecraftCapesConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MinecraftCapes implements ClientModInitializer {

	public static final String MODID = "minecraftcapes";
	@Getter private static final Logger Logger = LogManager.getLogger(MODID);
	private static KeyBinding keyBinding;
	
	@Override
	public void onInitializeClient() {
		getLogger().info("[MinecraftCapes] Initialising");

		//Loading Config
		MinecraftCapesConfig.loadConfig();

		//Configure the KeyBind
//		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
//			"key.minecraftcapes.gui",
//			InputUtil.Type.KEYSYM,
//			GLFW.GLFW_KEY_J,
//			"category.minecraftcapes.gui"
//		));

		//React to key pressed
//		ClientTickEvents.END_CLIENT_TICK.register(client -> {
//			while(keyBinding.wasPressed()) {
//				Minecraft.getInstance().openScreen(new MenuScreen(new TranslatableText("category.minecraftcapes.gui")));
//			}
//		});

		getLogger().info("[MinecraftCapes] Initialised");
	}
}
