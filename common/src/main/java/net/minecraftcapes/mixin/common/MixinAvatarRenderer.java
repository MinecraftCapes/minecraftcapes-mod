package net.minecraftcapes.mixin.common;

import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.ExtendedRenderState;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class MixinAvatarRenderer<AvatarlikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {
    
    public MixinAvatarRenderer(EntityRendererProvider.Context context, PlayerModel model, float shadow) {
        super(context, model, shadow);
    }
    
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At(value = "TAIL"))
    public void minecraftcapes$extractRenderState(AvatarlikeEntity avatarlikeEntity, AvatarRenderState avatarRenderState, float partialTicks, CallbackInfo ci) {
        ExtendedRenderState extendedRenderState = (ExtendedRenderState) avatarRenderState;
        PlayerHandler playerHandler = PlayerHandler.get(avatarlikeEntity.getUUID());
        
        if(playerHandler.getHasInfo()) {
            avatarRenderState.isUpsideDown = playerHandler.isUpsideDown();
            
            // We do a double check here because of the @WrapCondition
            // if isHasEars is true but isEarsVisible is false it will render with vanilla
            // The double check ensure we only render ears if they're visible
            avatarRenderState.showExtraEars = avatarRenderState.showExtraEars || (playerHandler.isHasEars() && MinecraftCapesConfig.isEarsVisible());
            
            extendedRenderState.minecraftcapes$setCapeEnabled(MinecraftCapesConfig.isCapeVisible());
            extendedRenderState.minecraftcapes$setGapeGlint(playerHandler.getHasCapeGlint());
            extendedRenderState.minecraftcapes$setEarsEnabled(MinecraftCapesConfig.isEarsVisible());
            extendedRenderState.minecraftcapes$setEarsTexture(playerHandler.getEarLocation());
        }
    }
}
