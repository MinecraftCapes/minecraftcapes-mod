package net.minecraftcapes.compatibility;

import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.power.ElytraFlightPower;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.player.PlayerHandler;

public class OriginsHook implements ICompatHooks {

    public OriginsHook() {
        MinecraftCapes.getLogger().info("Hooked into Origins");
        CompatHooks.addHook(this);
    }

    public void onPlayerRender(PlayerEntity playerEntity) {
        PlayerHandler playerHandler = PlayerHandler.getFromPlayer(playerEntity);
        if(OriginComponent.getPowers(playerEntity, ElytraFlightPower.class).stream().anyMatch(ElytraFlightPower::shouldRenderElytra)) {
            playerHandler.setShowCape(false);
            playerHandler.setForceShowElytra(true);
        }
    }
}
