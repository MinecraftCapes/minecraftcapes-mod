package net.minecraftcapes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraftcapes.ExtendedPlayerRenderState;
import net.minecraftcapes.player.CapeGlintManager;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CapeLayer.class)
public class MixinCapeLayer {

    @Redirect(method = "render*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    private VertexConsumer addCapeGlint(MultiBufferSource bufferSource, RenderType renderType, @Local(argsOnly = true) PlayerRenderState playerRenderState) {
        // Retrieve the player handler from playerRenderState
        PlayerHandler playerHandler = ((ExtendedPlayerRenderState) playerRenderState).getMinecraftCapes$playerHandler();

        // Redirect to custom buffer if player has cape glint, otherwise use the default buffer
        if (playerHandler.getHasCapeGlint()) {
            return CapeGlintManager.getCapeBuffer(bufferSource, RenderType.armorCutoutNoCull(playerRenderState.skin.capeTexture()));
        } else {
            return bufferSource.getBuffer(RenderType.entitySolid(playerRenderState.skin.capeTexture()));
        }
    }
}
