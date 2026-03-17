package net.minecraftcapes.mixin.lunar;

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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(PlayerInfo.class)
public class MixinPlayerInfo {
    
    @Inject(method = "<init>", at = @At("RETURN"))
    private void minecraftcapes$init(GameProfile profile, boolean enforcesSecureChat, CallbackInfo ci) {
        DownloadManager.prepareDownload(profile.id(), profile.name(), false);
    }

}
