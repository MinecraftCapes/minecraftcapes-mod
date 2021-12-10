package net.minecraftcapes;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftcapes.compatibility.ArmorVisibilityHook;
import net.minecraftcapes.compatibility.OriginsHook;
import net.minecraftcapes.compatibility.TrinketsHook;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.gui.MenuScreen;
import org.lwjgl.glfw.GLFW;

public class MinecraftCapesFabric implements ClientModInitializer {

	private static KeyMapping keyBinding;
	
	@Override
	public void onInitializeClient() {
        MinecraftCapesConstants.LOG.info("Initialising");

		//Loading Config
		MinecraftCapesConfig.loadConfig();

		//Configure the KeyBind
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
			"key.minecraftcapes.gui",
            InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_J,
			"category.minecraftcapes.gui"
		));

		//React to key pressed
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while(keyBinding.isDown()) {
				Minecraft.getInstance().setScreen(new MenuScreen());
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
        
        MinecraftCapesConstants.LOG.info("Initialised");
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
