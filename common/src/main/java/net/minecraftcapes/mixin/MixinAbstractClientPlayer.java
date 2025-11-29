package net.minecraftcapes.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer {

    @Shadow
    protected abstract PlayerInfo getPlayerInfo();

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void minecraftcapes$downloadPlayerInfo(ClientLevel clientLevel, GameProfile gameProfile, CallbackInfo ci) {
        DownloadManager.prepareDownload(gameProfile.getId(), gameProfile.getName(), false);
    }

    @Inject(method = "isCapeLoaded", at = @At(value = "HEAD"), cancellable = true)
    private void minecraftcapes$hasCape(CallbackInfoReturnable<Boolean> cir) {
        PlayerHandler playerHandler = PlayerHandler.get(((AbstractClientPlayer) (Object) this).getUUID());
        if(playerHandler.getHasInfo() || this.getPlayerInfo() != null) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getCloakTextureLocation", at = @At(value = "RETURN"), cancellable = true)
    private void minecraftcapes$getLocationCape(CallbackInfoReturnable<ResourceLocation> cir) {
        PlayerInfo networkplayerinfo = this.getPlayerInfo();
        PlayerHandler playerHandler = PlayerHandler.get(((AbstractClientPlayer) (Object) this).getUUID());
        if(playerHandler.getCapeLocation() != null && MinecraftCapesConfig.isCapeVisible()) {
            cir.setReturnValue(playerHandler.getCapeLocation());
        } else if(networkplayerinfo != null) {
            cir.setReturnValue(networkplayerinfo.getCapeLocation());
        } else {
            cir.setReturnValue(null);
        }
    }

}