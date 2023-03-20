package net.minecraftcapes.compatibility;

import net.minecraft.world.entity.player.Player;
import net.minecraftcapes.MinecraftCapesConstants;

public class OriginsHook implements ICompatHooks {
    
    public OriginsHook() {
        MinecraftCapesConstants.LOG.info("Hooked into Origins");
        MinecraftCapesConstants.LOG.info("-- NOT IMPLEMENTED -- ");
        CompatHooks.addHook(this);
    }
    
    public void onPlayerRender(Player player) {}
}