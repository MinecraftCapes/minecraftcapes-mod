package net.minecraftcapes.fabric;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class FabricServer implements DedicatedServerModInitializer {
    
    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            LogUtils.getLogger().error("=============================================");
            LogUtils.getLogger().error("MinecraftCapes only needs to be on the client");
            LogUtils.getLogger().error("     You'll still see each others capes!");
            LogUtils.getLogger().error("     Please remove this from your server!");
            LogUtils.getLogger().error("=============================================");
        });
    }
}
