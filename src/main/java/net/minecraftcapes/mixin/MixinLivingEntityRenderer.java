package net.minecraftcapes.mixin;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderLivingBase.class)
public abstract class MixinLivingEntityRenderer<T extends EntityLivingBase> extends Render<T> {

    protected MixinLivingEntityRenderer(RenderManager renderManager) {
        super(renderManager);
    }

    @Inject(method = "rotateCorpse", at = @At("RETURN"))
    public void renderUpsidedown(T bat, float p_77043_2_, float p_77043_3_, float partialTicks, CallbackInfo ci) {
        if(bat instanceof EntityPlayer) {
            PlayerHandler playerHandler = PlayerHandler.get(bat.getUniqueID());
            if(playerHandler.isUpsideDown()) {
                GlStateManager.translate(0.0F, bat.height + 0.1F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }
}
