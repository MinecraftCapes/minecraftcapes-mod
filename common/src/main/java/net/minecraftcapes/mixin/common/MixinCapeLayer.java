package net.minecraftcapes.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraftcapes.player.ExtendedRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = CapeLayer.class, priority = 0)
public abstract class MixinCapeLayer extends RenderLayer<PlayerRenderState, PlayerModel> {

    public MixinCapeLayer(RenderLayerParent<PlayerRenderState, PlayerModel> p_117346_) {
        super(p_117346_);
    }

    @WrapOperation(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/PlayerRenderState;FF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    private VertexConsumer minecraftcapes$renderGlint(MultiBufferSource instance, RenderType renderType, Operation<VertexConsumer> original, @Local(argsOnly = true) PlayerRenderState playerRenderState) {
        ExtendedRenderState minecraftcapes$playerRenderState = (ExtendedRenderState) playerRenderState;
        if(minecraftcapes$playerRenderState.minecraftcapes$getCapeEnabled()) {
            return ItemRenderer.getArmorFoilBuffer(instance, RenderType.armorCutoutNoCull(playerRenderState.skin.capeTexture()), minecraftcapes$playerRenderState.minecraftcapes$hasCapeGlint());
        } else {
            return original.call(instance, renderType);
        }
    }
}