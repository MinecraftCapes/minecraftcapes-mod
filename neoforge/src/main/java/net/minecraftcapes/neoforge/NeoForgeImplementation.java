package net.minecraftcapes.neoforge;

import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.neoforge.events.KeyHandlerEvent;
import net.minecraftcapes.neoforge.events.RegisterKeyEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;

@Mod(MinecraftCapes.MOD_ID)
public class NeoForgeImplementation {
    
    public NeoForgeImplementation() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
    }
    
    /**
     * This client setup event
     */
    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        MinecraftCapes.onEnable(FMLPaths.CONFIGDIR.get());
        
        //Register the events
        NeoForge.EVENT_BUS.register(KeyHandlerEvent.class);
        NeoForge.EVENT_BUS.register(RegisterKeyEvent.class);
    }
    
    /**
     * This is the server setup event
     * @param event
     */
    public void serverSetup(FMLDedicatedServerSetupEvent event) {
        MinecraftCapes.getLogger().error("=============================================");
        MinecraftCapes.getLogger().error("MinecraftCapes only needs to be on the client");
        MinecraftCapes.getLogger().error("     You'll still see each others capes!");
        MinecraftCapes.getLogger().error("     Please remove this from your server!");
        MinecraftCapes.getLogger().error("=============================================");
    }
}
