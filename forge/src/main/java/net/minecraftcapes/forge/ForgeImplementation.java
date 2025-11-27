package net.minecraftcapes.forge;

import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.forge.events.KeyHandlerEvent;
import net.minecraftcapes.forge.events.RegisterKeyEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(MinecraftCapes.MOD_ID)
public class ForgeImplementation {
    
    public ForgeImplementation(FMLJavaModLoadingContext context) {
        context.getModEventBus().addListener(this::clientSetup);
        context.getModEventBus().addListener(this::serverSetup);
    }
    
    /**
     * This client setup event
     * @param event
     */
    public void clientSetup(FMLClientSetupEvent event) {
        MinecraftCapes.onEnable(FMLPaths.CONFIGDIR.get());
        
        //Register the events
        MinecraftForge.EVENT_BUS.register(RegisterKeyEvent.class);
        MinecraftForge.EVENT_BUS.register(KeyHandlerEvent.class);
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
