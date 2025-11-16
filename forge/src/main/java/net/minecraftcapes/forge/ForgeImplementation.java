package net.minecraftcapes.forge;

import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.forge.events.KeyHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

@Mod(MinecraftCapes.MOD_ID)
public class ForgeImplementation extends MinecraftCapes {

    public ForgeImplementation() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }
    
    /**
     * This client setup event
     * @param event
     */
    public void clientSetup(FMLClientSetupEvent event) {
        MinecraftCapes.onEnable(FMLPaths.CONFIGDIR.get());
        
        //Register the events
        MinecraftForge.EVENT_BUS.register(new KeyHandlerEvent());

        //Register the key
        ClientRegistry.registerKeyBinding(MinecraftCapes.KEY_MAPPING);
        
        MinecraftCapes.getLogger().info("Initialised");
    }
    
}
