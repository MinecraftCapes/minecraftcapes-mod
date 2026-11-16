package net.minecraftcapes.mixin;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.util.ResourceLocation;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerCape.class)
public class MixinCapeLayer {

    @Final
    @Shadow
    private RenderPlayer playerRenderer;

    @Unique
    private ResourceLocation minecraftcapes$ENCHANTED_ITEM_GLINT_RES;

    @Inject(method = "doRenderLayer(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPlayer;renderCape(F)V", shift = At.Shift.AFTER), cancellable = true)
    public void minecraftcapes$addCapeGlint(AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale, CallbackInfo ci) {
        // Why? This fixes lunar, Unsafe.allocateInstance bypasses the constructor.
        if(minecraftcapes$ENCHANTED_ITEM_GLINT_RES == null) {
            minecraftcapes$ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
        }

        // Retrieve the player handler from playerRenderState
        PlayerHandler playerHandler = PlayerHandler.get(entitylivingbaseIn.getUniqueID());

        // Redirect to custom buffer if player has cape glint, otherwise use the default buffer
        if (MinecraftCapesConfig.isCapeVisible() && playerHandler.getHasCapeGlint()) {
            minecraftcapes$renderEchantmentGlint(entitylivingbaseIn, partialTicks);
        }
    }

    @Unique
    private void minecraftcapes$renderEchantmentGlint(AbstractClientPlayer player, float partialTicks) {
        float f = (float) player.ticksExisted + partialTicks;

        this.playerRenderer.bindTexture(minecraftcapes$ENCHANTED_ITEM_GLINT_RES);

        GlStateManager.enableBlend();
        GlStateManager.depthFunc(GL11.GL_EQUAL);
        GlStateManager.depthMask(false);
        GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);

        for (int i = 0; i < 2; ++i) {
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);

            float f2 = 0.76F;
            GlStateManager.color(0.5F * f2, 0.25F * f2, 0.8F * f2, 1.0F);

            GlStateManager.matrixMode(GL11.GL_TEXTURE);
            GlStateManager.loadIdentity();

            float f3 = 0.33333334F;
            GlStateManager.scale(f3, f3, f3);
            GlStateManager.rotate(30.0F - i * 60.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(0.0F, f * (0.001F + i * 0.003F) * 20.0F, 0.0F);

            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            this.playerRenderer.getMainModel().renderCape(0.0625F);
        }

        GlStateManager.matrixMode(GL11.GL_TEXTURE);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);

        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(GL11.GL_LEQUAL);

        // Restore standard rendering state
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Restore player texture
        this.playerRenderer.bindTexture(player.getLocationSkin());
    }

}
