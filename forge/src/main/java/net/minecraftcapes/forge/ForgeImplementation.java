package net.minecraftcapes.forge;

import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.forge.client.ClientForgeEvents;
import net.minecraftcapes.forge.client.ClientModEvents;
import net.minecraftcapes.forge.server.ServerModEvents;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MinecraftCapes.MOD_ID)
public class ForgeImplementation {

    private BusGroup busGroup;
    
    public ForgeImplementation(FMLJavaModLoadingContext context) {
        busGroup = context.getModBusGroup();
        FMLClientSetupEvent.getBus(busGroup).addListener(this::clientSetup);
        FMLDedicatedServerSetupEvent.getBus(busGroup).addListener(this::serverSetup);
    }
    
    /**
     * This client setup event
     */
    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MinecraftCapes.onEnable();

            //Register the events
            TickEvent.ClientTickEvent.Pre.BUS.addListener(ClientForgeEvents::onClickTick);
            RegisterKeyMappingsEvent.getBus(busGroup).addListener(ClientModEvents::registerKeyBinding);
            
            MinecraftCapes.getLogger().info("Initialised");
        });
    }
    
    /**
     * This client setup event
     */
    @SubscribeEvent
    public void serverSetup(FMLDedicatedServerSetupEvent event) {
        event.enqueueWork(() -> {
            //Register the events
            MinecraftForge.EVENT_BUS.register(ServerModEvents.class);
        });
    }
}