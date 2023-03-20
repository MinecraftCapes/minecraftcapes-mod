package net.minecraftcapes.player.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;

public class CapeLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

   public CapeLayer(EntityRenderer p_i50950_1_) {
      super((RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) p_i50950_1_);
   }

   public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      if(!MinecraftCapesConfig.isCapeVisible() && entitylivingbaseIn.getCloakTextureLocation() == null) return;

      PlayerHandler playerHandler = PlayerHandler.getFromPlayer(entitylivingbaseIn);
      if(playerHandler.getShowCape()) {
         if (!entitylivingbaseIn.isInvisible() && (entitylivingbaseIn.getCloakTextureLocation() != null || playerHandler.getCapeLocation() != null)) {
            ItemStack itemStack = entitylivingbaseIn.getItemBySlot(EquipmentSlot.CHEST);
             if(itemStack.getItem() != Items.ELYTRA || (playerHandler.getForceHideElytra() && !playerHandler.getForceShowElytra())) {
               matrixStackIn.pushPose();
               matrixStackIn.translate(0.0D, 0.0D, 0.125D);
               double d0 = Mth.lerp((double) partialTicks, entitylivingbaseIn.xCloakO, entitylivingbaseIn.xCloak) - Mth.lerp((double) partialTicks, entitylivingbaseIn.xo, entitylivingbaseIn.getX());
               double d1 = Mth.lerp((double) partialTicks, entitylivingbaseIn.yCloakO, entitylivingbaseIn.yCloak) - Mth.lerp((double) partialTicks, entitylivingbaseIn.yo, entitylivingbaseIn.getY());
               double d2 = Mth.lerp((double) partialTicks, entitylivingbaseIn.zCloakO, entitylivingbaseIn.zCloak) - Mth.lerp((double) partialTicks, entitylivingbaseIn.zo, entitylivingbaseIn.getZ());
               float f = entitylivingbaseIn.yBodyRotO + (entitylivingbaseIn.yBodyRot - entitylivingbaseIn.yBodyRotO);
               double d3 = (double) Mth.sin(f * ((float) Math.PI / 180F));
               double d4 = (double) (-Mth.cos(f * ((float) Math.PI / 180F)));
               float f1 = (float) d1 * 10.0F;
               f1 = Mth.clamp(f1, -6.0F, 32.0F);
               float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
               f2 = Mth.clamp(f2, 0.0F, 150.0F);
               float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;
               f3 = Mth.clamp(f3, -20.0F, 20.0F);
               if (f2 < 0.0F) {
                  f2 = 0.0F;
               }

               float f4 = Mth.lerp(partialTicks, entitylivingbaseIn.oBob, entitylivingbaseIn.bob);
               f1 = f1 + Mth.sin(Mth.lerp(partialTicks, entitylivingbaseIn.walkDistO, entitylivingbaseIn.walkDist) * 6.0F) * 32.0F * f4;
               if (entitylivingbaseIn.isCrouching()) {
                  f1 += 25.0F;
               }

               matrixStackIn.mulPose(Axis.XP.rotationDegrees(6.0F + f2 / 2.0F + f1));
               matrixStackIn.mulPose(Axis.ZP.rotationDegrees(f3 / 2.0F));
               matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F - f3 / 2.0F));
               VertexConsumer vertexConsumer;
               if(MinecraftCapesConfig.isCapeVisible() && playerHandler.getCapeLocation() != null) {
                  vertexConsumer = ItemRenderer.getArmorFoilBuffer(bufferIn, RenderType.entityTranslucent(playerHandler.getCapeLocation()), false, playerHandler.getHasCapeGlint());
               } else {
                  vertexConsumer = ItemRenderer.getArmorFoilBuffer(bufferIn, RenderType.entityTranslucent(entitylivingbaseIn.getCloakTextureLocation()), false, false);
               }
               this.getParentModel().renderCloak(matrixStackIn, vertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY);
               matrixStackIn.popPose();
            }
         }
      }
   }
}