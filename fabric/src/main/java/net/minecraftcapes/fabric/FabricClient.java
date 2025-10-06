package net.minecraftcapes.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.gui.MenuScreen;

public class FabricClient extends MinecraftCapes implements ClientModInitializer {
	private static KeyMapping keyBinding;
	@Override
	public void onInitializeClient() {
        MinecraftCapes.onEnable();

		//Configure the KeyBind
		keyBinding = KeyBindingHelper.registerKeyBinding(MinecraftCapes.KEY_MAPPING.get());

		//React to key pressed
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while(keyBinding.isDown()) {
				Minecraft.getInstance().setScreen(new MenuScreen());
			}
		});

		//Do Mod compatibility checks :(
//		if(doesClassExist("dev.emi.trinkets.TrinketsMain")) {
//			new TrinketsHook();
//		}

//		if(doesClassExist("com.trikzon.armor_visibility.ArmorVisibility")) {
//			new ArmorVisibilityHook();
//		}

//		if(doesClassExist("io.github.apace100.origins.Origins")) {
//			new OriginsHook();
//		}
        
        MinecraftCapes.getLogger().info("Initialised");
	}

	/**
	 * Checks if a class exists or not
	 * @param name The name of the class
	 * @return Whether the class exists
	 */
	private boolean doesClassExist(String name) {
		try {
			Class<?> c = Class.forName(name);
			System.out.println(c);
            return true;
        } catch (ClassNotFoundException ignored) {}
		return false;
	}
    
}
