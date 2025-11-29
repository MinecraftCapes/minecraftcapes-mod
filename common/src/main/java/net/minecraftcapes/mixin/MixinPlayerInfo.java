package net.minecraftcapes.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraftcapes.player.DownloadManager;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(PlayerInfo.class)
public class MixinPlayerInfo {

    @Shadow
    @Final
    private GameProfile profile;

    @Inject(method = "createSkinLookup", at = @At("HEAD"))
    private static void minecraftcapes$downloadPlayerInfo(GameProfile profile, CallbackInfoReturnable<Supplier<PlayerSkin>> cir) {
        DownloadManager.prepareDownload(profile.getId(), profile.getName(), false);
    }

    @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
    public void minecraftcapes$getSkin(CallbackInfoReturnable<PlayerSkin> cir) {
        PlayerHandler playerHandler = PlayerHandler.get(profile.getId());
        //Check player handler is loaded
        if(playerHandler.getHasInfo()) {
            cir.setReturnValue(playerHandler.getSkin(cir.getReturnValue()));
        }
    }

}
