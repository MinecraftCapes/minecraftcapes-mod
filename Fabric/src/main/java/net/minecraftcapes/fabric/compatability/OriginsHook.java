package net.minecraftcapes.fabric.compatability;

import net.minecraft.world.entity.player.Player;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.compatibility.CompatHooks;
import net.minecraftcapes.compatibility.ICompatHooks;

public class OriginsHook implements ICompatHooks {
    
    public OriginsHook() {
        MinecraftCapes.getLogger().info("Hooked into Origins");
        MinecraftCapes.getLogger().info("-- NOT IMPLEMENTED -- ");
        CompatHooks.addHook(this);
    }
    
    public void onPlayerRender(Player player) {}
}