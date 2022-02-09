package net.minecraftcapes;

import lombok.Getter;
import net.fabricmc.api.ClientModInitializer;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.options.KeyBinding;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.gui.MenuScreen;
import net.minecraftcapes.helpers.GetMinecraftInstance;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.keyboard.KeyStateChangedEvent;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

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
		StationAPI.EVENT_BUS.register(this);

		getLogger().info("[MinecraftCapes] Initialised");
	}

	@EventListener
	private void onKeyBinding(KeyBindingRegisterEvent event) {
		event.keyBindings.add(new KeyBinding("MinecraftCapes Menu", 36));
	}

	@EventListener
	private void onKeyPress(KeyStateChangedEvent event) {
		if(event.environment != KeyStateChangedEvent.Environment.IN_GAME) return;
		if(Keyboard.getEventKey() == 36) {
			GetMinecraftInstance.getMinecraftInstance().openScreen(new MenuScreen());
		}
	}
}
