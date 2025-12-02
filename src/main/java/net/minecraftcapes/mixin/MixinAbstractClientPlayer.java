package net.minecraftcapes.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
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
    protected abstract NetworkPlayerInfo getPlayerInfo();

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void minecraftcapes$downloadPlayerInfo(World worldIn, GameProfile playerProfile, CallbackInfo ci) {
        DownloadManager.prepareDownload(playerProfile.getId(), playerProfile.getName(), false);
    }

    @Inject(method = "hasPlayerInfo", at = @At(value = "HEAD"), cancellable = true)
    private void minecraftcapes$updatePlayerInfo(CallbackInfoReturnable<Boolean> cir) {
        PlayerHandler playerHandler = PlayerHandler.get(((AbstractClientPlayer) (Object) this).getUniqueID());
        if(playerHandler.getHasInfo() || this.getPlayerInfo() != null) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getLocationCape", at = @At(value = "RETURN"), cancellable = true)
    private void minecraftcapes$getLocationCape(CallbackInfoReturnable<ResourceLocation> cir) {
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        PlayerHandler playerHandler = PlayerHandler.get(((AbstractClientPlayer) (Object) this).getUniqueID());
        if(playerHandler.getCapeLocation() != null && MinecraftCapesConfig.isCapeVisible()) {
            cir.setReturnValue(playerHandler.getCapeLocation());
        } else if(networkplayerinfo != null) {
            cir.setReturnValue(networkplayerinfo.getLocationCape());
        } else {
            cir.setReturnValue(null);
        }
    }

}
