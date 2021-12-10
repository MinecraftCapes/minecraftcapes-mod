package net.minecraftcapes.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftcapes.player.DownloadManager;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinPlayerJoinWorld extends Player {
    
    public MixinPlayerJoinWorld(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }
    
    @Inject(method = "<init>*", at = @At("RETURN"))
	private void construct(ClientLevel clientLevel, GameProfile gameProfile, CallbackInfo info) {
        if(this.getLevel().isClientSide()) {
            PlayerHandler playerHandler = PlayerHandler.getFromPlayer(this);
            if(playerHandler == null || playerHandler.getHasInfo()) return;
            DownloadManager.downloadProfile(playerHandler);
        }
	}

}
