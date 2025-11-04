package net.minecraftcapes.mixin;

import net.minecraft.client.entity.ClientAvatarState;
import net.minecraft.client.entity.ClientMannequin;
import net.minecraftcapes.ExtendedAvatarRenderState;
import net.minecraftcapes.player.DownloadManager;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientMannequin.class)
public class ClientMannequinMixin {

    @Shadow
    private final ClientAvatarState avatarState = new ClientAvatarState();

    @Inject(method = "updateSkin", at = @At("HEAD"))
    public void updateSkin(CallbackInfo ci) {
        ExtendedAvatarRenderState extendedPlayerRenderState = (ExtendedAvatarRenderState) avatarState;
        PlayerHandler playerHandler = extendedPlayerRenderState.getMinecraftCapes$playerHandler();
        DownloadManager.prepareDownload(playerHandler);
    }
}
