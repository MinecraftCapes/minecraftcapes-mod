package net.minecraftcapes.compatibility;

import net.minecraft.util.ResourceLocation;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.player.PlayerHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.caelus.api.RenderElytraEvent;

public class CaelusHook {

    public CaelusHook() {
        MinecraftCapes.getLogger().info("Hooked into Caelus");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void renderElytra(RenderElytraEvent event) {
        PlayerHandler playerHandler = PlayerHandler.getFromPlayer(event.getPlayer());
        ResourceLocation location = playerHandler.getCapeLocation();
        if(location != null) {
            event.setResourceLocation(location);
        }

        playerHandler.setShowCape(!event.canRender());
    }

}
