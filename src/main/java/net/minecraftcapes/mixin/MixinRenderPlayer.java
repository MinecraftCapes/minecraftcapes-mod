package net.minecraftcapes.mixin;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftcapes.player.PlayerHandler;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RendererLivingEntity.class)
public abstract class MixinRenderPlayer extends Render {

    @Inject(method = "rotateCorpse", at = @At(value = "RETURN"))
    public void minecraftcapes$renderUpsideDown(EntityLivingBase entityLivingBase, float p_77043_2_, float p_77043_3_, float p_77043_4_, CallbackInfo ci) {
        if(entityLivingBase instanceof EntityPlayer) {
            PlayerHandler playerHandler = PlayerHandler.get(entityLivingBase.getUniqueID());
            if (playerHandler.isUpsideDown()) {
                GL11.glTranslatef(0.0F, entityLivingBase.height + 0.1F, 0.0F);
                GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }
}
