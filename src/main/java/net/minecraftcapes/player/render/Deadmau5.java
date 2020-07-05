package net.minecraftcapes.player.render;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftcapes.player.PlayerHandler;

public class Deadmau5 implements LayerRenderer<AbstractClientPlayer> {

   private final RenderPlayer playerRenderer;

   public Deadmau5(RenderPlayer playerRendererIn) {
      this.playerRenderer = playerRendererIn;
   }

   @Override
   public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      PlayerHandler playerHandler = PlayerHandler.getFromPlayer(entitylivingbaseIn);
      if (playerHandler.getEarLocation() != null && entitylivingbaseIn.hasSkin() && !entitylivingbaseIn.isInvisible()) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.playerRenderer.bindTexture(playerHandler.getEarLocation());
         GlStateManager.pushMatrix();
         if(entitylivingbaseIn.isSneaking()) {
            GlStateManager.translate(0.0F, 0.25F, 0.0F);
         }
         GlStateManager.scale(1.3333334F, 1.3333334F, 1.3333334F);
         this.playerRenderer.getMainModel().renderDeadmau5Head(0.0625F);
         GlStateManager.popMatrix();
      }
   }
   @Override
   public boolean shouldCombineTextures() {
      return true;
   }
}
