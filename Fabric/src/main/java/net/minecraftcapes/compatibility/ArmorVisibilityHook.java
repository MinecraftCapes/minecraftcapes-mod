package net.minecraftcapes.compatibility;

import com.trikzon.armor_visibility.ArmorVisibility;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftcapes.MinecraftCapesConstants;
import net.minecraftcapes.player.PlayerHandler;

public class ArmorVisibilityHook implements ICompatHooks {

    public ArmorVisibilityHook() {
        MinecraftCapesConstants.LOG.info("Hooked into ArmorVisibility");
        CompatHooks.addHook(this);
    }

    public void onPlayerRender(Player player) {
        PlayerHandler playerHandler = PlayerHandler.getFromPlayer(player);
        if(ArmorVisibility.saveFile.hideAllArmorToggle || (ArmorVisibility.saveFile.hideMyArmorToggle && player.equals(Minecraft.getInstance().player))) {
            playerHandler.setForceHideElytra(true);
        } else {
            playerHandler.setForceHideElytra(false);
        }
    }
}
