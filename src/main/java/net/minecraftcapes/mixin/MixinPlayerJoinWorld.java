package net.minecraftcapes.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.Session;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraftcapes.events.PlayerEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinPlayerJoinWorld extends PlayerBase {

	public MixinPlayerJoinWorld(Level arg) {
		super(arg);
	}

	@Inject(method = "<init>*", at = @At("RETURN"))
	private void construct(Minecraft level, Level session, Session dimensionId, int par4, CallbackInfo ci) {
		PlayerEventHandler.onPlayerJoin(this);
	}

}
