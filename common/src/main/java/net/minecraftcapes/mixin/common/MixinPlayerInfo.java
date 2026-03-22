package net.minecraftcapes.mixin.common;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraftcapes.player.DownloadManager;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInfo.class)
public abstract class MixinPlayerInfo {

    @Shadow
    @Final
    private GameProfile profile;
    
    @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
    public void minecraftcapes$getSkin(CallbackInfoReturnable<PlayerSkin> cir) {
        PlayerHandler playerHandler = PlayerHandler.get(profile.id());
        playerHandler.setName(profile.name());
        
        //Check player handler is loaded
        if(playerHandler.getHasInfo()) {
            cir.setReturnValue(playerHandler.getSkin(cir.getReturnValue()));
        } else {
            DownloadManager.prepareDownload(playerHandler);
        }
    }

}
