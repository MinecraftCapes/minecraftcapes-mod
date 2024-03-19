package net.minecraftcapes.neoforge;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.neoforge.client.ClientForgeEvents;
import net.minecraftcapes.neoforge.client.ClientModEvents;
import net.minecraftcapes.neoforge.server.ServerModEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(MinecraftCapes.MOD_ID)
public class NeoForgeImplementation {
    
    IEventBus modEventBus;
    
    public NeoForgeImplementation(IEventBus modEventBus) {
        this.modEventBus = modEventBus;
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::serverSetup);
    }
    
    /**
     * This client setup event
     */
    public void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MinecraftCapes.onEnable();
            
            //Try turn on capes
            Minecraft.getInstance().options.toggleModelPart(PlayerModelPart.CAPE, true);
            
            //Register the events
            NeoForge.EVENT_BUS.register(ClientForgeEvents.class);
            modEventBus.register(ClientModEvents.class); //EVENT BUS ERROR HERE
            
            MinecraftCapes.getLogger().info("Initialised");
        });
    }
    
    /**
     * This client setup event
     */
    public void serverSetup(FMLDedicatedServerSetupEvent event) {
        event.enqueueWork(() -> {
            //Register the events
            NeoForge.EVENT_BUS.register(ServerModEvents.class);
        });
    }
}
