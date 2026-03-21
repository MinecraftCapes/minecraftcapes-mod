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
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraftcapes.player.ExtendedRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CapeLayer.class)
public abstract class MixinCapeLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    
    public MixinCapeLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer) {
        super(renderer);
    }
    
    @WrapWithCondition(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"))
    private boolean minecraftcapes$submitCape(SubmitNodeCollector instance, Model model, Object state, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, int outlineColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, @Local(argsOnly = true) AvatarRenderState avatarRenderState) {
        ExtendedRenderState extendedRenderState = (ExtendedRenderState) avatarRenderState;
        if(extendedRenderState.minecraftcapes$getCapeEnabled()) {
            instance.order(0).submitModel(model, state, poseStack, RenderTypes.armorCutoutNoCull(avatarRenderState.skin.cape().texturePath()), lightCoords, overlayCoords, -1, null, outlineColor, crumblingOverlay);
            if(extendedRenderState.minecraftcapes$hasCapeGlint()) {
                instance.order(1).submitModel(model, state, poseStack, RenderTypes.armorEntityGlint(), lightCoords, overlayCoords, -1, null, outlineColor, crumblingOverlay);
            }
            return false;
        } else {
            return true;
        }
    }
}