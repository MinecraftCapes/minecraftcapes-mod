package net.minecraftcapes.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftcapes.events.PlayerEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class MixinPlayerJoinWorld extends PlayerEntity {
	
	public MixinPlayerJoinWorld(ClientWorld world, GameProfile gameProfile) {
		super(world, gameProfile);
	}

	@Inject(method = "<init>*", at = @At("RETURN"))
	private void construct(ClientWorld clientWorld, GameProfile gameProfile, CallbackInfo info) {
		PlayerEventHandler.onPlayerJoin(this);
	}

}
