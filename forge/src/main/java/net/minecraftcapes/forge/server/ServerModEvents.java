package net.minecraftcapes.forge.server;

import com.mojang.logging.LogUtils;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MinecraftCapes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.DEDICATED_SERVER)
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
