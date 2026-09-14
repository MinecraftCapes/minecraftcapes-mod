package net.minecraftcapes.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.living.player.ClientPlayerEntity;
import net.minecraft.client.network.PlayerInfo;
import net.minecraft.resource.Identifier;
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

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity {

    @Shadow
    protected abstract PlayerInfo getInfo();

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void minecraftcapes$downloadPlayerInfo(World worldIn, GameProfile playerProfile, CallbackInfo ci) {
        DownloadManager.prepareDownload(playerProfile.getId(), playerProfile.getName(), false);
    }

    @Inject(method = "hasInfo", at = @At(value = "HEAD"), cancellable = true)
    private void minecraftcapes$updatePlayerInfo(CallbackInfoReturnable<Boolean> cir) {
        PlayerHandler playerHandler = PlayerHandler.get(((ClientPlayerEntity) (Object) this).getUuid());
        if(playerHandler.getHasInfo() || this.getInfo() != null) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getCapeTextureLocation", at = @At(value = "RETURN"), cancellable = true)
    private void minecraftcapes$getLocationCape(CallbackInfoReturnable<Identifier> cir) {
        PlayerInfo networkplayerinfo = this.getInfo();
        PlayerHandler playerHandler = PlayerHandler.get(((ClientPlayerEntity) (Object) this).getUuid());
        if(playerHandler.getCapeLocation() != null && MinecraftCapesConfig.isCapeVisible()) {
            cir.setReturnValue(playerHandler.getCapeLocation());
        } else if(networkplayerinfo != null) {
            cir.setReturnValue(networkplayerinfo.getCapeTexture());
        } else {
            cir.setReturnValue(null);
        }
    }

}
