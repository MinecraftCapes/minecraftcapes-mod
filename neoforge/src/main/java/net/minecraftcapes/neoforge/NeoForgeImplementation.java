package net.minecraftcapes.neoforge;

import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.neoforge.client.ClientForgeEvents;
import net.minecraftcapes.neoforge.server.ServerModEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(MinecraftCapes.MOD_ID)
public class NeoForgeImplementation {
    
    public NeoForgeImplementation(IEventBus modEventBus) {
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::serverSetup);
    }
    
    /**
     * This client setup event
     */
    public void clientSetup(FMLClientSetupEvent event) {
        MinecraftCapes.onEnable();
        
        //Register the events
        NeoForge.EVENT_BUS.register(ClientForgeEvents.class);
        
        MinecraftCapes.getLogger().info("Initialised");
    }
    
    /**
     * This is server setup
     */
    public void serverSetup(FMLDedicatedServerSetupEvent event) {
        NeoForge.EVENT_BUS.register(ServerModEvents.class);
    }
}
