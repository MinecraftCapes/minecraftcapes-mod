package net.minecraftcapes.mixin;

import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraftcapes.ExtendedAvatarRenderState;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("HEAD"))
    private void addPlayerHandler(AvatarlikeEntity avatarlikeEntity, AvatarRenderState avatarRenderState, float p_445702_, CallbackInfo ci) {
        ExtendedAvatarRenderState extendedPlayerRenderState = (ExtendedAvatarRenderState) avatarRenderState;
        extendedPlayerRenderState.setMinecraftCapes$playerHandler(PlayerHandler.get(avatarlikeEntity));
    }

    @Inject(method = "isEntityUpsideDown(Lnet/minecraft/world/entity/Avatar;)Z", at = @At("HEAD"), cancellable = true)
    private void isEntityUpsideDown(AvatarlikeEntity entity, CallbackInfoReturnable<Boolean> cir) {
        PlayerHandler playerHandler = PlayerHandler.get(entity);
        if(playerHandler.getHasInfo()) {
            cir.setReturnValue(playerHandler.isUpsideDown());
        }
    }

}
