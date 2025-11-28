package net.minecraftcapes.forge;

import net.minecraftcapes.MinecraftCapes;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(MinecraftCapes.MOD_ID)
public class ForgeImplementation {
    
    private final BusGroup busGroup;
    
    public ForgeImplementation(FMLJavaModLoadingContext context) {
        busGroup = context.getModBusGroup();
        FMLClientSetupEvent.getBus(busGroup).addListener(this::clientSetup);
        FMLDedicatedServerSetupEvent.getBus(busGroup).addListener(this::serverSetup);
    }
    
    /**
     * This client setup event
     * @param event
     */
    public void clientSetup(FMLClientSetupEvent event) {
        MinecraftCapes.onEnable(FMLPaths.CONFIGDIR.get());
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
