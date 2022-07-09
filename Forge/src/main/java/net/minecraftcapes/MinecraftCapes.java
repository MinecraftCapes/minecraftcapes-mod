package net.minecraftcapes;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.events.AddLayersEvent;
import net.minecraftcapes.events.KeyHandlerEvent;
import net.minecraftcapes.events.PlayerEventHandler;
import net.minecraftcapes.events.PlayerRenderEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MinecraftCapesConstants.MOD_ID)
public class MinecraftCapes {
    
    public static KeyMapping menuKey;
    
    public MinecraftCapes() {
        MinecraftCapesConstants.LOG.info("Initialising");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
        MinecraftCapesConstants.LOG.info("Initialised");
    }
    
    /**
     * This client setup event
     * @param event
     */
    public void clientSetup(FMLClientSetupEvent event) {
        //Loading Config
        MinecraftCapesConfig.loadConfig();
        
        //Register the events
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerRenderEvent());
        MinecraftForge.EVENT_BUS.register(new KeyHandlerEvent());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(AddLayersEvent::construct);
        
        //Try turn on capes
        Minecraft.getInstance().options.toggleModelPart(PlayerModelPart.CAPE, true);
    
        //Register the key
        MinecraftCapes.menuKey = new KeyMapping("key.minecraftcapes.gui", 74, "category.minecraftcapes.gui");
    }
    
    /**
     * Register the keybinds
     * @param event
     */
    @SubscribeEvent
    public void registerKeyBinding(RegisterKeyMappingsEvent event) {
        event.register(menuKey);
    }
    
    /**
     * The Server Started Event
     * @param event
     */
    public void serverSetup(FMLDedicatedServerSetupEvent event) {
        MinecraftCapesConstants.LOG.error("MinecraftCapes has been loaded on server side. MinecraftCapes is a client only mod. No need to worry about this. You can delete the mod if you wish!");
    }
    
}
