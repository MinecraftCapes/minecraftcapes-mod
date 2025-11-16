package net.minecraftcapes.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.gui.MenuScreen;

public class FabricClient extends MinecraftCapes implements ClientModInitializer {
	private static KeyMapping keyBinding;
	
	@Override
	public void onInitializeClient() {
        MinecraftCapes.onEnable(FabricLoaderImpl.INSTANCE.getConfigDir());

		//Configure the KeyBind
		keyBinding = KeyBindingHelper.registerKeyBinding(MinecraftCapes.KEY_MAPPING);

		//React to key pressed
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while(keyBinding.isDown()) {
				Minecraft.getInstance().setScreen(new MenuScreen());
			}
		});
        
        MinecraftCapes.getLogger().info("Initialised");
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
