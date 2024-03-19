package net.minecraftcapes.fabric.compatability;

import net.minecraft.world.entity.player.Player;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.compatibility.CompatHooks;
import net.minecraftcapes.compatibility.ICompatHooks;

public class ArmorVisibilityHook implements ICompatHooks {

    public ArmorVisibilityHook() {
        MinecraftCapes.getLogger().info("Hooked into ArmorVisibility");
        CompatHooks.addHook(this);
    }

    public void onPlayerRender(Player player) {
//        PlayerHandler playerHandler = PlayerHandler.get(player);
//        ArmorVisibilityOptions options = ArmorVisibility.OPTIONS.get();
//        if(options.saveData.hideAllArmor || (options.saveData.hideMyArmor && player.equals(Minecraft.getInstance().player))) {
//            playerHandler.setForceHideElytra(true);
//        } else {
//            playerHandler.setForceHideElytra(false);
//        }
    }
}
