package net.minecraftcapes;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraftcapes.compatibility.CaelusHook;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.events.AddLayersEvent;
import net.minecraftcapes.events.KeyHandlerEvent;
import net.minecraftcapes.events.PlayerEventHandler;
import net.minecraftcapes.events.PlayerRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MinecraftCapes.MODID)
public class MinecraftCapes {

	public static final String MODID = "minecraftcapes";
	@Getter private static final Logger logger = LogManager.getFormatterLogger("MinecraftCapes");
	public static KeyBinding menuKey;

	public MinecraftCapes() {
		getLogger().info("Initialising");
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
		getLogger().info("Initialised");
	}

	/**
	 * This client setup event
	 * @param event
	 */
	public void clientSetup(FMLClientSetupEvent event) {
		//Loading Config
		MinecraftCapesConfig.loadConfig();

		//Do Mod compatibility checks :(
		if(doesClassExist("top.theillusivec4.caelus.api.CaelusApi")) {
			new CaelusHook();
		}

		//Register the events
		MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerRenderEvent());
		MinecraftForge.EVENT_BUS.register(new KeyHandlerEvent());
		FMLJavaModLoadingContext.get().getModEventBus().addListener(AddLayersEvent::construct);

		//Try turn on capes
		Minecraft.getInstance().gameSettings.setModelPartEnabled(PlayerModelPart.CAPE, true);

		//Register the keybinds
		MinecraftCapes.menuKey = new KeyBinding("key.minecraftcapes.gui", 74, "category.minecraftcapes.gui");
		ClientRegistry.registerKeyBinding(menuKey);
	}

	/**
	 * The Server Started Event
	 * @param event
	 */
	public void serverSetup(FMLDedicatedServerSetupEvent event) {
		MinecraftCapes.getLogger().error("MinecraftCapes has been loaded on server side. MinecraftCapes is a client only mod. No need to worry about this. You can delete the mod if you wish!");
	}

	/**
	 * Check if a class exists
	 * @param name full name of class
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
