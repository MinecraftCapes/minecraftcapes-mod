package net.minecraftcapes.mixin.lunar;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraftcapes.player.DownloadManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInfo.class)
public class MixinPlayerInfo {
    
    @Inject(method = "<init>", at = @At("RETURN"))
    private void minecraftcapes$init(GameProfile profile, boolean p_254409_, CallbackInfo ci) {
        DownloadManager.prepareDownload(profile.getId(), profile.getName(), false);
    }

}
