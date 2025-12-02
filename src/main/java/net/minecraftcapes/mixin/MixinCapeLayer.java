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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public abstract class MixinCapeLayer extends RendererLivingEntity {

    @Shadow
    public ModelBiped modelBipedMain;

    @Unique
    private static final ResourceLocation minecraftcapes$ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    public MixinCapeLayer(ModelBase p_i1261_1_, float p_i1261_2_) {
        super(p_i1261_1_, p_i1261_2_);
    }

    @Inject(method = "renderEquippedItems(Lnet/minecraft/client/entity/AbstractClientPlayer;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBiped;renderCloak(F)V", shift = At.Shift.AFTER))
    public void minecraftcapes$addCapeGlint(AbstractClientPlayer entitylivingbaseIn, float partialTicks, CallbackInfo ci) {
        // Retrieve the player handler from playerRenderState
        PlayerHandler playerHandler = PlayerHandler.get(entitylivingbaseIn.getUniqueID());

        // Redirect to custom buffer if player has cape glint, otherwise use the default buffer
        if (MinecraftCapesConfig.isCapeVisible() && playerHandler.getHasCapeGlint()) {
            minecraftcapes$renderEnchantmentGlint(entitylivingbaseIn, partialTicks);
        }
    }

    @Unique
    private void minecraftcapes$renderEnchantmentGlint(EntityLivingBase entitylivingbaseIn, float partialTicks) {
        float f8 = (float)entitylivingbaseIn.ticksExisted + partialTicks;
        this.bindTexture(minecraftcapes$ENCHANTED_ITEM_GLINT_RES);
        GL11.glEnable(GL11.GL_BLEND);
        float f9 = 0.5F;
        GL11.glColor4f(f9, f9, f9, 1.0F);
        GL11.glDepthFunc(GL11.GL_EQUAL);
        GL11.glDepthMask(false);

        for (int k = 0; k < 2; ++k)
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            float f10 = 0.76F;
            GL11.glColor4f(0.5F * f10, 0.25F * f10, 0.8F * f10, 1.0F);
            GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glLoadIdentity();
            float f11 = f8 * (0.001F + (float)k * 0.003F) * 20.0F;
            float f12 = 0.33333334F;
            GL11.glScalef(f12, f12, f12);
            GL11.glRotatef(30.0F - (float)k * 60.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(0.0F, f11, 0.0F);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            this.modelBipedMain.renderCloak(0.0625F);
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
