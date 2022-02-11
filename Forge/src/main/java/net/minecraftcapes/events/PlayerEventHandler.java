package net.minecraftcapes.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftcapes.player.DownloadManager;
import net.minecraftcapes.player.PlayerHandler;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerEventHandler {
    
    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {
        if(event.getEntity() instanceof Player && event.getWorld().isClientSide()) {
            DownloadManager.prepareDownload((Player) event.getEntity(), false);
        }
    }
}