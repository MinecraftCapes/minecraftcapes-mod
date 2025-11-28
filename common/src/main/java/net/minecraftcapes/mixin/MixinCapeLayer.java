package net.minecraftcapes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraftcapes.player.ExtendedRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CapeLayer.class)
public abstract class MixinCapeLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    
    public MixinCapeLayer(RenderLayerParent<AvatarRenderState, PlayerModel> p_117346_) {
        super(p_117346_);
    }
    
    @Redirect(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"))
    private void submit(SubmitNodeCollector instance, Model model, Object object, PoseStack poseStack, RenderType renderType, int p_432874_, int overlay, int outlineColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, @Local AvatarRenderState avatarRenderState) {
        ExtendedRenderState extendedRenderState = (ExtendedRenderState) avatarRenderState;
        if(extendedRenderState.minecraftcapes$getCapeEnabled()) {
            instance.order(0).submitModel(model, object, poseStack, RenderTypes.armorCutoutNoCull(avatarRenderState.skin.cape().texturePath()), p_432874_, overlay, -1, null, outlineColor, crumblingOverlay);
            if(extendedRenderState.minecraftcapes$hasCapeGlint()) {
                instance.order(1).submitModel(model, object, poseStack,  RenderTypes.armorEntityGlint(), p_432874_, overlay, -1, null, outlineColor, crumblingOverlay);
            }
        } else {
            instance.submitModel(
                    model,
                    avatarRenderState,
                    poseStack,
                    renderType,
                    p_432874_,
                    overlay,
                    outlineColor,
                    crumblingOverlay
            );
        }
    }
}