package net.minecraftcapes.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FabricServer implements DedicatedServerModInitializer {
    
    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            Logger logger = LogManager.getLogger();
            logger.warn("=============================================");
            logger.warn("MinecraftCapes only needs to be on the client");
            logger.warn( "     You'll still see each others capes!");
            logger.warn("     Please remove this from your server!");
            logger.warn("=============================================");
        });
    }
}