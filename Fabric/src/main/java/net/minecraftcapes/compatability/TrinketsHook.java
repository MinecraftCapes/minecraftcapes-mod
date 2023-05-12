package net.minecraftcapes.compatability;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftcapes.MinecraftCapesConstants;
import net.minecraftcapes.compatibility.CompatHooks;
import net.minecraftcapes.compatibility.ICompatHooks;
import net.minecraftcapes.player.PlayerHandler;

import java.util.Optional;

public class TrinketsHook implements ICompatHooks {

    public TrinketsHook() {
        MinecraftCapesConstants.LOG.info("Hooked into Trinkets");
        CompatHooks.addHook(this);
    }

    public void onPlayerRender(Player player) {
        PlayerHandler playerHandler = PlayerHandler.get(player);
        Optional<TrinketComponent> trinketComponent = TrinketsApi.getTrinketComponent(player);
        if(trinketComponent.isPresent() && trinketComponent.get().getAllEquipped().contains(Items.ELYTRA)) {
            playerHandler.setShowCape(false);
            playerHandler.setForceShowElytra(true);
        } else {
            playerHandler.setShowCape(true);
            playerHandler.setForceShowElytra(false);
        }
    }
}
