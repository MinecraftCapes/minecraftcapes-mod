package co.uk.minecraftcapes.player.render;

import org.lwjgl.opengl.GL11;

import co.uk.minecraftcapes.player.PlayerInfo;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class RenderDeadmau5 extends RenderPlayer {
	
	@SubscribeEvent
	public void renderLayerDeadmau5(RenderPlayerEvent.Specials.Pre event) {		
		AbstractClientPlayer entitylivingbaseIn = (AbstractClientPlayer) event.entity;
    	ResourceLocation rl = PlayerInfo.getEarResourceLocation(entitylivingbaseIn);
    	float partialTicks = event.partialRenderTick;
    	    
    	if(!entitylivingbaseIn.isInvisible() && rl != null) {
    		Minecraft.getMinecraft().getTextureManager().bindTexture(rl);
    		
    		float f2; 
	    	for (int i = 0; i < 2; ++i)
	        {           	    		
                float f9 = entitylivingbaseIn.prevRotationYaw + (entitylivingbaseIn.rotationYaw - entitylivingbaseIn.prevRotationYaw) * partialTicks - (entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks);
                float f10 = entitylivingbaseIn.prevRotationPitch + (entitylivingbaseIn.rotationPitch - entitylivingbaseIn.prevRotationPitch) * partialTicks;
                GL11.glPushMatrix();
                GL11.glRotatef(f9, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(f10, 1.0F, 0.0F, 0.0F);
                GL11.glTranslatef(0.375F * (float)(i * 2 - 1), 0.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.375F, 0.0F);
                GL11.glRotatef(-f10, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-f9, 0.0F, 1.0F, 0.0F);
                f2 = 1.3333334F;
                GL11.glScalef(f2, f2, f2);
                //new ModelDeadmau5(event.renderer.modelBipedMain).modeldeadmau5.render(0.0625F);
                event.renderer.modelBipedMain.renderEars(0.0625F);
                GL11.glPopMatrix();
	        }
	    } 
    }
}
