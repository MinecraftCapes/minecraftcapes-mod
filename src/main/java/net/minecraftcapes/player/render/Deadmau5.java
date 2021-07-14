package net.minecraftcapes.player.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;

public class Deadmau5 extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

   public Deadmau5(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> p_i50950_1_) {
      super(p_i50950_1_);
   }

   public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      if(!MinecraftCapesConfig.isEarsVisible()) return;

      PlayerHandler playerHandler = PlayerHandler.getFromPlayer(entitylivingbaseIn);
      if (playerHandler.getEarLocation() != null && entitylivingbaseIn.hasSkin() && !entitylivingbaseIn.isInvisible()) {
         IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntitySolid(playerHandler.getEarLocation()));
         int i = LivingRenderer.getPackedOverlay(entitylivingbaseIn, 0.0F);

         matrixStackIn.push();
         if(entitylivingbaseIn.isCrouching()) {
            matrixStackIn.translate(0.0F, 0.25F, 0.0F);
         }
         matrixStackIn.scale(1.3333334F, 1.3333334F, 1.3333334F);
         this.getEntityModel().renderEars(matrixStackIn, ivertexbuilder, packedLightIn, i);
         matrixStackIn.pop();
      }
   }
}
