package net.minecraftcapes.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerBase;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftcapes.events.PlayerEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayerBase.class)
public abstract class MixinPlayerJoinWorld extends PlayerBase {

	public MixinPlayerJoinWorld(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}

	@Inject(method = "<init>*", at = @At("RETURN"))
	private void construct(ClientWorld clientWorld, GameProfile gameProfile, CallbackInfo info) {
		PlayerEventHandler.onPlayerJoin(this);
	}

}
