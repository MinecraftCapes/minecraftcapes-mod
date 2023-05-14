package net.minecraftcapes.fabric.compatability;

import com.diontryban.armor_visibility.client.ArmorVisibilityClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.compatibility.CompatHooks;
import net.minecraftcapes.compatibility.ICompatHooks;
import net.minecraftcapes.player.PlayerHandler;

public class ArmorVisibilityHook implements ICompatHooks {

    public ArmorVisibilityHook() {
        MinecraftCapes.getLogger().info("Hooked into ArmorVisibility");
        CompatHooks.addHook(this);
    }

    public void onPlayerRender(Player player) {
        PlayerHandler playerHandler = PlayerHandler.get(player);
        if(ArmorVisibilityClient.hideAllArmor || (ArmorVisibilityClient.hideMyArmor && player.equals(Minecraft.getInstance().player))) {
            playerHandler.setForceHideElytra(true);
        } else {
            playerHandler.setForceHideElytra(false);
        }
    }
}
