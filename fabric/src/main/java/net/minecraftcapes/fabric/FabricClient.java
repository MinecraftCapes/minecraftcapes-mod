package net.minecraftcapes.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
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
		keyBinding = KeyMappingHelper.registerKeyMapping(MinecraftCapes.KEY_MAPPING);

		//React to key pressed
		ClientTickEvents.END_CLIENT_TICK.register(_ -> {
			while(keyBinding.consumeClick()) {
				Minecraft.getInstance().setScreen(new MenuScreen());
			}
		});
	}
}
