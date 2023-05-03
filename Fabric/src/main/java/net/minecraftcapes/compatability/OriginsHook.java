package net.minecraftcapes.compatability;

import net.minecraft.world.entity.player.Player;
import net.minecraftcapes.MinecraftCapesConstants;
import net.minecraftcapes.compatibility.CompatHooks;
import net.minecraftcapes.compatibility.ICompatHooks;

public class OriginsHook implements ICompatHooks {
    
    public OriginsHook() {
        MinecraftCapesConstants.LOG.info("Hooked into Origins");
        MinecraftCapesConstants.LOG.info("-- NOT IMPLEMENTED -- ");
        CompatHooks.addHook(this);
    }
    
    public void onPlayerRender(Player player) {}
}