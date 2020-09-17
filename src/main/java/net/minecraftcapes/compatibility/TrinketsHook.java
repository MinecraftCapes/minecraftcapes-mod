package net.minecraftcapes.compatibility;

import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Items;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.player.PlayerHandler;

public class TrinketsHook implements ICompatHooks {

    public TrinketsHook() {
        MinecraftCapes.getLogger().info("Hooked into Trinkets");
        CompatHooks.addHook(this);
    }

    public void onPlayerRender(PlayerEntity playerEntity) {
        PlayerHandler playerHandler = PlayerHandler.getFromPlayer(playerEntity);
        Inventory inventory = TrinketsApi.getTrinketsInventory(playerEntity);
        if(inventory.count(Items.ELYTRA) > 0) {
            playerHandler.setShowCape(false);
            playerHandler.setForceShowElytra(true);
        } else {
            playerHandler.setShowCape(true);
            playerHandler.setForceShowElytra(false);
        }
    }
}
