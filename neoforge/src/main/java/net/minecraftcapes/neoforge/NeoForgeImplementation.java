package net.minecraftcapes.neoforge;

import net.minecraftcapes.MinecraftCapes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(MinecraftCapes.MOD_ID)
public class NeoForgeImplementation {
    
    public NeoForgeImplementation(IEventBus modEventBus) {
        modEventBus.addListener(this::clientSetup);
    }
    
    /**
     * This client setup event
     */
    public void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MinecraftCapes.onEnable();

            MinecraftCapes.getLogger().info("Initialised");
        });
    }
}
