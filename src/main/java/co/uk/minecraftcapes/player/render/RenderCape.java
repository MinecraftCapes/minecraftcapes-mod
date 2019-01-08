package co.uk.minecraftcapes.player.render;

import org.ietf.jgss.GSSManager;
import org.lwjgl.opengl.GL11;

import co.uk.minecraftcapes.player.PlayerInfo;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class RenderCape extends RenderPlayer {

	@SubscribeEvent
	public void renderLayerCape(RenderPlayerEvent.Specials.Pre event) {
		
		AbstractClientPlayer entitylivingbaseIn = (AbstractClientPlayer) event.entityPlayer;		
		ResourceLocation rl = PlayerInfo.getCapeResourceLocation(entitylivingbaseIn);		
		float partialTicks = event.partialRenderTick;
		
		System.out.println(rl);
		
		if(!entitylivingbaseIn.isInvisible() && rl != null) {							      
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

            if (f5 < -6.0F)
            {
                f5 = -6.0F;
            }

            if (f5 > 32.0F)
            {
                f5 = 32.0F;
            }

            float f6 = (float)(d3 * d1 + d0 * d2) * 100.0F;
            float f7 = (float)(d3 * d2 - d0 * d1) * 100.0F;

            if (f6 < 0.0F)
            {
                f6 = 0.0F;
            }

            float f8 = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
            f5 += MathHelper.sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f8;

            if (entitylivingbaseIn.isSneaking())
            {
                f5 += 25.0F;
            }

            GL11.glRotatef(6.0F + f6 / 2.0F + f5, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f7 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f7 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            this.modelBipedMain.renderCloak(0.0625F);
            GL11.glPopMatrix();
		}
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return PlayerInfo.getCapeResourceLocation((AbstractClientPlayer) p_110775_1_);
	}	
}
