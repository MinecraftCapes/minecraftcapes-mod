package net.minecraftcapes.player.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;

public class Deadmau5 extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

   public Deadmau5(EntityRenderer p_i50950_1_) {
      super((RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) p_i50950_1_);
   }

   public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      if(!MinecraftCapesConfig.isEarsVisible()) return;

      PlayerHandler playerHandler = PlayerHandler.getFromPlayer(entitylivingbaseIn);
      if (playerHandler.getEarLocation() != null && entitylivingbaseIn.isSkinLoaded() && !entitylivingbaseIn.isInvisible()) {
         VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entitySolid(playerHandler.getEarLocation()));
         int i = LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F);

         matrixStackIn.pushPose();
         if(entitylivingbaseIn.isCrouching()) {
            matrixStackIn.translate(0.0F, 0.25F, 0.0F);
         }
         matrixStackIn.scale(1.3333334F, 1.3333334F, 1.3333334F);
         this.getParentModel().renderEars(matrixStackIn, ivertexbuilder, packedLightIn, i);
         matrixStackIn.popPose();
      }
   }
}