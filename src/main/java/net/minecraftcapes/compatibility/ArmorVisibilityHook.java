package net.minecraftcapes.compatibility;

import com.trikzon.armor_visibility.ArmorVisibility;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.player.PlayerHandler;

public class ArmorVisibilityHook implements ICompatHooks {

    public ArmorVisibilityHook() {
        MinecraftCapes.getLogger().info("Hooked into ArmorVisibility");
        CompatHooks.addHook(this);
    }

    public void onPlayerRender(PlayerEntity playerEntity) {
        PlayerHandler playerHandler = PlayerHandler.getFromPlayer(playerEntity);
        if(ArmorVisibility.CONFIG.allArmorInvis || (ArmorVisibility.CONFIG.myArmorInvis && playerEntity.equals(MinecraftClient.getInstance().player))) {
            playerHandler.setForceHideElytra(true);
        } else {
            playerHandler.setForceHideElytra(false);
        }
    }
}
