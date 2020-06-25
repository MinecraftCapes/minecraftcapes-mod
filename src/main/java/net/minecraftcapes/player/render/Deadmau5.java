package net.minecraftcapes.player.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftcapes.player.PlayerHandler;

public class Deadmau5 extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

   public Deadmau5(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> p_i50950_1_) {
      super(p_i50950_1_);
   }

   @Override
   public void render(AbstractClientPlayerEntity entityIn, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_) {
      PlayerHandler playerHandler = PlayerHandler.getFromPlayer(entityIn);
      ResourceLocation rl = playerHandler.getEarLocation();
      if (rl != null && entityIn.hasSkin() && !entityIn.isInvisible()) {
         this.bindTexture(rl);
         GlStateManager.pushMatrix();
         if(entityIn.isSneaking()) {
            GlStateManager.translatef(0.0F, 0.25F, 0.0F);
         }
         GlStateManager.scalef(1.3333334F, 1.3333334F, 1.3333334F);
         this.getEntityModel().renderDeadmau5Head(0.0625F);
         GlStateManager.popMatrix();
      }
   }

   @Override
   public boolean shouldCombineTextures() {
      return false;
   }
}
