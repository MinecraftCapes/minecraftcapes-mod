package net.minecraftcapes.forge;

import net.minecraftcapes.MinecraftCapes;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MinecraftCapes.MOD_ID)
public class ForgeImplementation {

    public ForgeImplementation(FMLJavaModLoadingContext context) {
        BusGroup busGroup = context.getModBusGroup();
        FMLClientSetupEvent.getBus(busGroup).addListener(this::clientSetup);
    }
    
    /**
     * This client setup event
     */
    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MinecraftCapes.onEnable();
            
            MinecraftCapes.getLogger().info("Initialised");
        });
    }
}