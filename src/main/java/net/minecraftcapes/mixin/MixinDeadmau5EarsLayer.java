package net.minecraftcapes.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.Deadmau5HeadLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Deadmau5HeadLayer.class)
public abstract class MixinDeadmau5EarsLayer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

    public MixinDeadmau5EarsLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> p_i50926_1_) {
        super(p_i50926_1_);
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/client/entity/player/AbstractClientPlayerEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    public void render(MatrixStack poseStack, IRenderTypeBuffer multiBufferSource, int packedLightIn, AbstractClientPlayerEntity abstractClientPlayer, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_, CallbackInfo ci) {
        //Cancel default render
        if(!abstractClientPlayer.getName().toString().equalsIgnoreCase("deadmau5")) {
            ci.cancel();
        }

        PlayerHandler playerHandler = PlayerHandler.get(abstractClientPlayer.getUUID());
        if (playerHandler.getEarLocation() != null && !abstractClientPlayer.isInvisible() && MinecraftCapesConfig.isEarsVisible()) {
            IVertexBuilder iVertexBuilder = multiBufferSource.getBuffer(RenderType.entitySolid(playerHandler.getEarLocation()));

            int i = LivingRenderer.getOverlayCoords(abstractClientPlayer, 0.0F);

            poseStack.pushPose();
            if(abstractClientPlayer.isCrouching()) {
                poseStack.translate(0.0F, 0.2F, 0.0F);
            }
            poseStack.scale(1.3333334F, 1.3333334F, 1.3333334F);
            this.getParentModel().renderEars(poseStack, iVertexBuilder, packedLightIn, i);
            poseStack.popPose();
        }
    }
}
