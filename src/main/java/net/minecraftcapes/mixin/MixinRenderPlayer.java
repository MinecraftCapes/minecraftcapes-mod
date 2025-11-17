package net.minecraftcapes.mixin;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer extends RendererLivingEntity {

    public MixinRenderPlayer(ModelBase p_i1261_1_, float p_i1261_2_) {
        super(p_i1261_1_, p_i1261_2_);
    }

    @Inject(method = "rotateCorpse(Lnet/minecraft/client/entity/AbstractClientPlayer;FFF)V", at = @At(value = "HEAD"))
    public void renderUpsidedown(AbstractClientPlayer p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_, CallbackInfo ci) {
        PlayerHandler playerHandler = PlayerHandler.get(p_77043_1_.getUniqueID());
        if(playerHandler.isUpsideDown()) {
            GL11.glTranslatef(0.0F, p_77043_1_.height + 0.1F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        }
    }
}
