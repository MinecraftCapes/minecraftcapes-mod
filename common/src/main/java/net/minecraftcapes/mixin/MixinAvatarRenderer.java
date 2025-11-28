package net.minecraftcapes.mixin;

import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.PlayerModel;
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
    
    public MixinAvatarRenderer(EntityRendererProvider.Context p_174289_, PlayerModel p_174290_, float p_174291_) {
        super(p_174289_, p_174290_, p_174291_);
    }
    
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At(value = "TAIL"))
    public void extractRenderState(AvatarlikeEntity avatarlikeEntity, AvatarRenderState avatarRenderState, float p_445702_, CallbackInfo ci) {
        ExtendedRenderState extendedRenderState = (ExtendedRenderState) avatarRenderState;
        PlayerHandler playerHandler = PlayerHandler.get(avatarlikeEntity.getUUID());
        
        if(playerHandler.getHasInfo()) {
            avatarRenderState.isUpsideDown = playerHandler.isUpsideDown();
            avatarRenderState.showExtraEars = MinecraftCapesConfig.isEarsVisible() && playerHandler.getEarLocation() != null;
            extendedRenderState.minecraftcapes$setCapeEnabled(MinecraftCapesConfig.isCapeVisible());
            extendedRenderState.minecraftcapes$setGapeGlint(playerHandler.getHasCapeGlint());
            extendedRenderState.minecraftcapes$setEarsTexture(playerHandler.getEarLocation());
        }
    }
}
