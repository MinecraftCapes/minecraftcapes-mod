package net.minecraftcapes;

import lombok.Getter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.TranslatableText;
import net.minecraftcapes.compatibility.ArmorVisibilityHook;
import net.minecraftcapes.compatibility.OriginsHook;
import net.minecraftcapes.compatibility.TrinketsHook;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.gui.MenuScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

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
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.minecraftcapes.gui",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_J,
			"category.minecraftcapes.gui"
		));

		//React to key pressed
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while(keyBinding.wasPressed()) {
				MinecraftClient.getInstance().openScreen(new MenuScreen(new TranslatableText("category.minecraftcapes.gui")));
			}
		});

		//Do Mod compatibility checks :(
		if(doesClassExist("dev.emi.trinkets.TrinketsMain")) {
			new TrinketsHook();
		}

		if(doesClassExist("com.trikzon.armor_visibility.ArmorVisibility")) {
			new ArmorVisibilityHook();
		}

		if(doesClassExist("io.github.apace100.origins.Origins")) {
			new OriginsHook();
		}

		getLogger().info("[MinecraftCapes] Initialised");
	}

	/**
	 * Checks if a class exists or not
	 * @param name
	 * @return
	 */
	private boolean doesClassExist(String name) {
		try {
			Class c = Class.forName(name);
			System.out.println(c);
			if (c != null) {
				return true;
			}
		} catch (ClassNotFoundException e) {}
		return false;
	}
}
