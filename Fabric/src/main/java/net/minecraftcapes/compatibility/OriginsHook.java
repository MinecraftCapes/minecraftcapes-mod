package net.minecraftcapes.compatibility;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ElytraFlightPower;
import net.minecraft.world.entity.player.Player;
import net.minecraftcapes.MinecraftCapesConstants;
import net.minecraftcapes.player.PlayerHandler;

public class OriginsHook implements ICompatHooks {

    public OriginsHook() {
        MinecraftCapesConstants.LOG.info("Hooked into Origins");
        CompatHooks.addHook(this);
    }

    public void onPlayerRender(Player player) {
        PlayerHandler playerHandler = PlayerHandler.getFromPlayer(player);
        if(PowerHolderComponent.getPowers(player, ElytraFlightPower.class).stream().anyMatch(ElytraFlightPower::shouldRenderElytra)) {
            playerHandler.setShowCape(false);
            playerHandler.setForceShowElytra(true);
        }
    }
}
