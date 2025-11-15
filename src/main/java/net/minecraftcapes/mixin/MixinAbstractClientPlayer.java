package net.minecraftcapes.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class MixinAbstractClientPlayer {

    @Shadow
    protected abstract NetworkPlayerInfo getPlayerInfo();

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void downloadCape(ClientWorld clientWorld, GameProfile gameProfile, CallbackInfo ci) {
        DownloadManager.prepareDownload(gameProfile.getId(), gameProfile.getName(), false);
    }

    @Inject(method = "getCloakTextureLocation", at = @At(value = "RETURN"), cancellable = true)
    private void getLocationCape(CallbackInfoReturnable<ResourceLocation> cir) {
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        PlayerHandler playerHandler = PlayerHandler.get(((AbstractClientPlayerEntity) (Object) this).getUUID());
        if(playerHandler.getCapeLocation() != null && MinecraftCapesConfig.isCapeVisible()) {
            cir.setReturnValue(playerHandler.getCapeLocation());
        } else if(networkplayerinfo != null) {
            cir.setReturnValue(networkplayerinfo.getCapeLocation());
        } else {
            cir.setReturnValue(null);
        }
    }

}
