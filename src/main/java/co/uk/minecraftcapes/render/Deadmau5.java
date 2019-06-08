package co.uk.minecraftcapes.render;

import com.mojang.blaze3d.platform.GlStateManager;

import co.uk.minecraftcapes.events.PlayerEventHandler;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.util.ResourceLocation;

public class Deadmau5 extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>
{
   public Deadmau5(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> p_i50950_1_) {
      super(p_i50950_1_);
   }

    public void func_212842_a_(AbstractClientPlayerEntity entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
    	ResourceLocation rl = PlayerEventHandler.getEarResourceLocation(entitylivingbaseIn);
    	if(!entitylivingbaseIn.isInvisible() && rl != null) {
    
    		this.func_215333_a(rl);
	            
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
                this.func_215332_c().renderDeadmau5Head(0.0625F);
                GlStateManager.popMatrix();
	        }
	    } 
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}
