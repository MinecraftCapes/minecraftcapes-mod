package net.minecraftcapes.mixin.common;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.Deadmau5EarsLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraftcapes.player.ExtendedRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Deadmau5EarsLayer.class)
public abstract class MixinDeadmau5EarsLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    
    public MixinDeadmau5EarsLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer) {
        super(renderer);
    }
    
    @WrapWithCondition(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;III)V"))
    public boolean minecraftcapes$submitEars(SubmitNodeCollector instance, Model model, Object state, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, int outlineColor, @Local(argsOnly = true) AvatarRenderState avatarRenderState) {
        ExtendedRenderState extendedRenderState = (ExtendedRenderState) avatarRenderState;
        if(extendedRenderState.minecraftcapes$getEarsEnabled()) {
            instance.submitModel(model, avatarRenderState, poseStack, RenderTypes.armorCutoutNoCull(extendedRenderState.minecraftcapes$getEarsTexture()), lightCoords, overlayCoords, outlineColor);
            return false;
        } else {
            return true;
        }
    }
}
