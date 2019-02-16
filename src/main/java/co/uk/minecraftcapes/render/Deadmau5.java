package co.uk.minecraftcapes.render;

import co.uk.minecraftcapes.events.PlayerEventHandler;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class Deadmau5 implements LayerRenderer<AbstractClientPlayer>
{
    private final RenderPlayer playerRenderer;

    public Deadmau5(RenderPlayer playerRendererIn)
    {
        this.playerRenderer = playerRendererIn;
    }

    public void render(AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
    	ResourceLocation rl = PlayerEventHandler.getEarResourceLocation(entitylivingbaseIn);
    	if(!entitylivingbaseIn.isInvisible() && rl != null) {
    
    		this.playerRenderer.bindTexture(rl);
	            
	    	for (int i = 0; i < 2; ++i)
	        {
	    		float d = 0F;
	            if (entitylivingbaseIn.isSneaking()) {
	            	d = 0.25F;
	            }
	            
                float f = entitylivingbaseIn.prevRotationYaw + (entitylivingbaseIn.rotationYaw - entitylivingbaseIn.prevRotationYaw) * partialTicks - (entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks);
                float f1 = entitylivingbaseIn.prevRotationPitch + (entitylivingbaseIn.rotationPitch - entitylivingbaseIn.prevRotationPitch) * partialTicks;
                
                GlStateManager.pushMatrix();
                GlStateManager.rotatef(f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(f1, 1.0F, 0.0F, 0.0F);
                GlStateManager.translatef(0.375F * (float)(i * 2 - 1), d, 0.0F);
                GlStateManager.translatef(0.0F, -0.375F, 0.0F);
                GlStateManager.rotatef(-f1, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(-f, 0.0F, 1.0F, 0.0F);
                float f2 = 1.3333334F;
                GlStateManager.scalef(f2, f2, f2);
                this.playerRenderer.getMainModel().renderDeadmau5Head(0.0625F);
                GlStateManager.popMatrix();
	        }
	    } 
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}
