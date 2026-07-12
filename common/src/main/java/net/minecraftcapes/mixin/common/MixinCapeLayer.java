package net.minecraftcapes.mixin.common;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraftcapes.player.ExtendedRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = CapeLayer.class, priority = 0)
public abstract class MixinCapeLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    
    public MixinCapeLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer) {
        super(renderer);
    }
    
    @WrapWithCondition(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;III)V"))
    private boolean minecraftcapes$submitCape(SubmitNodeCollector instance, Model model, Object state, PoseStack poseStack, RenderType oldRenderType, int lightCoords, int overlayCoords, int outlineColor, @Local(argsOnly = true) AvatarRenderState avatarRenderState) {
        ExtendedRenderState extendedRenderState = (ExtendedRenderState) avatarRenderState;
        if(extendedRenderState.minecraftcapes$getCapeEnabled()) {
            Identifier capeTexture = avatarRenderState.skin.cape().texturePath();
            RenderType renderType = extendedRenderState.minecraftcapes$hasCapeGlint() ? RenderTypes.armorCutoutNoCullGlint(capeTexture) : RenderTypes.armorCutoutNoCull(capeTexture);
            instance.submitModel(model, state, poseStack, renderType, lightCoords, overlayCoords, outlineColor);
            return false;
        } else {
            return true;
        }
    }
}