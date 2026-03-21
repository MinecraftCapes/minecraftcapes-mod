package net.minecraftcapes.mixin.common;

import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.entity.ClientMannequin;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.level.Level;
import net.minecraftcapes.player.DownloadManager;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientMannequin.class)
public abstract class MixinClientMannequin extends Mannequin implements ClientAvatarEntity {
    
    protected MixinClientMannequin(Level p_445957_) {
        super(p_445957_);
    }

    @Inject(method = "updateSkin", at = @At("RETURN"))
    public void minecraftcapes$updateSkin(CallbackInfo ci) {
        PlayerHandler playerHandler = PlayerHandler.get(this.uuid);
        
        //Check player handler is loaded
        if(!playerHandler.getHasInfo()) {
            this.minecraftcapes$loadProfile(playerHandler);
        }
    }
    
    @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
    public void minecraftcapes$getSkin(CallbackInfoReturnable<PlayerSkin> cir) {
        PlayerHandler playerHandler = PlayerHandler.get(this.uuid);
        
        //Check player handler is loaded
        if(playerHandler.getHasInfo()) {
            cir.setReturnValue(playerHandler.getSkin(cir.getReturnValue()));
        } else {
            this.minecraftcapes$loadProfile(playerHandler);
        }
    }
    
    @Unique
    private void minecraftcapes$loadProfile(PlayerHandler playerHandler) {
        playerHandler.setPlayerUUID(this.getProfile().partialProfile().id());
        playerHandler.setName(this.getProfile().partialProfile().name());
        DownloadManager.prepareDownload(playerHandler);
    }
}