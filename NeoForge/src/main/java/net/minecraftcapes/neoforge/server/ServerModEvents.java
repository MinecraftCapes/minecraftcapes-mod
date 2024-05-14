package net.minecraftcapes.neoforge.server;

import com.mojang.logging.LogUtils;
import net.minecraftcapes.MinecraftCapes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@EventBusSubscriber(modid = MinecraftCapes.MOD_ID, value = Dist.DEDICATED_SERVER)
public class ServerModEvents {
    
    @SubscribeEvent
    public static void serverSetup(ServerStartedEvent event) {
        LogUtils.getLogger().error("=============================================");
        LogUtils.getLogger().error("MinecraftCapes only needs to be on the client");
        LogUtils.getLogger().error("     You'll still see each others capes!");
        LogUtils.getLogger().error("     Please remove this from your server!");
        LogUtils.getLogger().error("=============================================");
    }
}