package co.uk.minecraftcapes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import co.uk.minecraftcapes.player.downloader.DownloadCape;
import co.uk.minecraftcapes.player.downloader.DownloadEars;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class MixinPlayerJoinWorld extends PlayerEntity {
	
	public MixinPlayerJoinWorld(World world_1, GameProfile gameProfile_1) {
		super(world_1, gameProfile_1); 
	}

	@Inject(method = "<init>*", at = @At("RETURN"))
	private void construct(ClientWorld clientWorld_1, GameProfile gameProfile_1, CallbackInfo info){
		String uuid = gameProfile_1.getId().toString().replace("-", "");
		DownloadCape.download(uuid);
		DownloadEars.download(uuid);		
	}

}
