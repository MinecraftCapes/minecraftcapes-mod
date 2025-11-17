package net.minecraftcapes.mixin;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public abstract class MixinDeadmau5EarsLayer extends RendererLivingEntity {

    @Shadow
    public ModelBiped modelBipedMain;

    public MixinDeadmau5EarsLayer(ModelBase p_i1261_1_, float p_i1261_2_) {
        super(p_i1261_1_, p_i1261_2_);
    }

    @Inject(method = "renderEquippedItems(Lnet/minecraft/client/entity/AbstractClientPlayer;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;hasCape()Z"))
    public void renderEars(AbstractClientPlayer entitylivingbaseIn, float p_77029_2_, CallbackInfo ci) {
        PlayerHandler playerHandler = PlayerHandler.get(entitylivingbaseIn.getUniqueID());
        if (playerHandler.getEarLocation() != null && !entitylivingbaseIn.isInvisible() && MinecraftCapesConfig.isEarsVisible()) {
            this.bindTexture(playerHandler.getEarLocation());

            GL11.glPushMatrix();
            float f2 = 1.3333334F;
            GL11.glScalef(f2, f2, f2);
            if(entitylivingbaseIn.isSneaking()) {
                GL11.glTranslatef(0.0F, 0.2F, 0.0F);
            }
            this.modelBipedMain.renderEars(0.0625F);
            GL11.glPopMatrix();
        }
    }

}
