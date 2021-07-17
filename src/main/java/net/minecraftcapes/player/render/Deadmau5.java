package net.minecraftcapes.player.render;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.lwjgl.opengl.GL11;

public class Deadmau5 extends RenderPlayer {

   @SubscribeEvent
   public void doRenderLayer(RenderPlayerEvent.Specials.Pre event) {
      if(!MinecraftCapesConfig.isEarsVisible()) return;

      event.renderer.modelBipedMain.bipedEars = new ModelRenderer(event.renderer.modelBipedMain, 24, 0);
      event.renderer.modelBipedMain.bipedEars.addBox(1.5F, -10.5F, -1.0F, 6, 6, 1, 0.0F);
      event.renderer.modelBipedMain.bipedEars.addBox(-7.5F, -10.5F, -1.0F, 6, 6, 1, 0.0F);
      event.renderer.modelBipedMain.bipedEars.setRotationPoint(0.0F, 0.0F, 0.0F);

      AbstractClientPlayer entitylivingbaseIn = (AbstractClientPlayer) event.entity;
      PlayerHandler playerHandler = PlayerHandler.getFromPlayer(entitylivingbaseIn);
      if (playerHandler.getEarLocation() != null && !entitylivingbaseIn.isInvisible()) {
         Minecraft.getMinecraft().getTextureManager().bindTexture(playerHandler.getEarLocation());
         GL11.glPushMatrix();
         if(entitylivingbaseIn.isSneaking()) {
            GL11.glTranslatef(0.0F, 0.25F, 0.0F);
         }
         GL11.glScalef(1.3333334F, 1.3333334F, 1.3333334F);
         event.renderer.modelBipedMain.renderEars(0.0625F);
         GL11.glPopMatrix();
      }
   }
}
