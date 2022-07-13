package net.minecraftcapes.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftcapes.player.DownloadManager;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerEventHandler {
    
    @SubscribeEvent
    public void onPlayerJoin(EntityJoinLevelEvent event) {
        if(event.getEntity() instanceof Player && event.getLevel().isClientSide()) {
            DownloadManager.prepareDownload((Player) event.getEntity(), false);
        }
    }
}