package net.minecraftcapes.player.render;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftcapes.player.PlayerHandler;
import net.minecraftcapes.player.model.ModelCape;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.lwjgl.opengl.GL11;

public class CapeLayer extends RenderPlayer {

   protected static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
   private final ModelCape modelCape = new ModelCape();

   @SubscribeEvent
   public void doRenderLayer(RenderPlayerEvent.Specials.Pre event) {
      AbstractClientPlayer entitylivingbaseIn = (AbstractClientPlayer) event.entityPlayer;
      float partialTicks = event.partialRenderTick;
      PlayerHandler playerHandler = PlayerHandler.getFromPlayer(entitylivingbaseIn);
      ResourceLocation rl = playerHandler.getCapeLocation();
      if (rl != null && !entitylivingbaseIn.isInvisible()) {
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         Minecraft.getMinecraft().getTextureManager().bindTexture(rl);
         GL11.glPushMatrix();
         GL11.glTranslatef(0.0F, 0.0F, 0.125F);
         double d3 = entitylivingbaseIn.field_71091_bM + (entitylivingbaseIn.field_71094_bP - entitylivingbaseIn.field_71091_bM) * (double)partialTicks - (entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * (double)partialTicks);
         double d4 = entitylivingbaseIn.field_71096_bN + (entitylivingbaseIn.field_71095_bQ - entitylivingbaseIn.field_71096_bN) * (double)partialTicks - (entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * (double)partialTicks);
         double d0 = entitylivingbaseIn.field_71097_bO + (entitylivingbaseIn.field_71085_bR - entitylivingbaseIn.field_71097_bO) * (double)partialTicks - (entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * (double)partialTicks);
         float f4 = entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks;
         double d1 = (double)MathHelper.sin(f4 * (float)Math.PI / 180.0F);
         double d2 = (double)(-MathHelper.cos(f4 * (float)Math.PI / 180.0F));
         float f5 = (float)d4 * 10.0F;

         if (f5 < -6.0F) {
            f5 = -6.0F;
         }

         if (f5 > 32.0F) {
            f5 = 32.0F;
         }

         float f6 = (float)(d3 * d1 + d0 * d2) * 100.0F;
         float f7 = (float)(d3 * d2 - d0 * d1) * 100.0F;

         if (f6 < 0.0F) {
            f6 = 0.0F;
         }

         float f8 = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
         f5 += MathHelper.sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f8;

         if (entitylivingbaseIn.isSneaking()) {
            f5 += 25.0F;
         }

         GL11.glRotatef(6.0F + f6 / 2.0F + f5, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(f7 / 2.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(-f7 / 2.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         this.modelCape.setRotationAngles(event.entityPlayer.limbSwing, event.entityPlayer.limbSwingAmount, event.entityPlayer.getAge(), event.entityPlayer.getRotationYawHead(), event.entityPlayer.cameraPitch, 0.0625F, entitylivingbaseIn);
         this.modelCape.render(entitylivingbaseIn, event.entityPlayer.limbSwing, event.entityPlayer.limbSwingAmount, event.entityPlayer.getAge(), event.entityPlayer.getRotationYawHead(),event.entityPlayer.cameraPitch, 0.0625F);
         if(playerHandler.getHasCapeGlint()) {
            renderEchantmentGlint(entitylivingbaseIn, this.modelCape, event.entityPlayer.limbSwing, event.entityPlayer.limbSwingAmount, partialTicks, event.entityPlayer.getAge(), event.entityPlayer.getRotationYawHead(), event.entityPlayer.cameraPitch, 0.0625F);
         }
         GL11.glPopMatrix();
      }
   }

   private void renderEchantmentGlint(AbstractClientPlayer entitylivingbaseIn, ModelBase modelbaseIn, float p_177183_3_, float p_177183_4_, float p_177183_5_, float p_177183_6_, float p_177183_7_, float p_177183_8_, float p_177183_9_)
   {
      float f = (float)entitylivingbaseIn.ticksExisted + p_177183_5_;
      Minecraft.getMinecraft().getTextureManager().bindTexture(ENCHANTED_ITEM_GLINT_RES);
      GL11.glEnable(GL11.GL_BLEND);
      float f9 = 0.5F;
      GL11.glColor4f(f9, f9, f9, 1.0F);
      GL11.glDepthFunc(GL11.GL_EQUAL);
      GL11.glDepthMask(false);

      for (int k = 0; k < 2; ++k)
      {
         GL11.glDisable(GL11.GL_LIGHTING);
         float f2 = 0.76F;
         GL11.glColor4f(0.5F * f2, 0.25F * f2, 0.8F * f2, 1.0F);
         GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
         GL11.glMatrixMode(GL11.GL_TEXTURE);
         GL11.glLoadIdentity();
         float f3 = 0.33333334F;
         GL11.glScalef(f3, f3, f3);
         GL11.glRotatef(30.0F - (float)k * 60.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(0.0F, f * (0.001F + (float)k * 0.003F) * 20.0F, 0.0F);
         GL11.glMatrixMode(GL11.GL_MODELVIEW);
         modelbaseIn.render(entitylivingbaseIn, p_177183_3_, p_177183_4_, p_177183_6_, p_177183_7_, p_177183_8_, p_177183_9_);
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glMatrixMode(GL11.GL_TEXTURE);
      GL11.glDepthMask(true);
      GL11.glLoadIdentity();
      GL11.glMatrixMode(GL11.GL_MODELVIEW);
      GL11.glEnable(GL11.GL_LIGHTING);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDepthFunc(GL11.GL_LEQUAL);
   }
}