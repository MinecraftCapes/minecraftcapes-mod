package net.minecraftcapes.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.Deadmau5EarsLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Deadmau5EarsLayer.class)
public abstract class MixinDeadmau5EarsLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public MixinDeadmau5EarsLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLightIn, AbstractClientPlayer abstractClientPlayer, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        //Cancel default render
        if(!abstractClientPlayer.getName().toString().equalsIgnoreCase("deadmau5")) {
            ci.cancel();
        }

        PlayerHandler playerHandler = PlayerHandler.get(abstractClientPlayer.getUUID());
        if (playerHandler.getEarLocation() != null && !abstractClientPlayer.isInvisible() && MinecraftCapesConfig.isEarsVisible()) {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entitySolid(playerHandler.getEarLocation()));

            int i = LivingEntityRenderer.getOverlayCoords(abstractClientPlayer, 0.0F);

            poseStack.pushPose();
            if(abstractClientPlayer.isCrouching()) {
                poseStack.translate(0.0F, 0.2F, 0.0F);
            }
            poseStack.scale(1.3333334F, 1.3333334F, 1.3333334F);
            this.getParentModel().renderEars(poseStack, vertexConsumer, packedLightIn, i);
            poseStack.popPose();
        }
    }
}
