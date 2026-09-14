package net.minecraftcapes.mixin;

import net.minecraft.client.entity.living.player.ClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.client.render.entity.layer.CapeLayer;
import net.minecraft.client.render.platform.GlStateManager;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.resource.Identifier;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeLayer.class)
public class MixinCapeLayer {

    @Final
    @Shadow
    private PlayerRenderer parent;

    @Unique
    private Identifier minecraftcapes$ENCHANTED_ITEM_GLINT_RES;

    @Inject(method = "render(Lnet/minecraft/client/entity/living/player/ClientPlayerEntity;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/entity/PlayerModel;renderCape(F)V", shift = At.Shift.AFTER))
    public void minecraftcapes$addCapeGlint(ClientPlayerEntity entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale, CallbackInfo ci) {
        // Why? This fixes lunar, Unsafe.allocateInstance bypasses the constructor.
        if(minecraftcapes$ENCHANTED_ITEM_GLINT_RES == null) {
            minecraftcapes$ENCHANTED_ITEM_GLINT_RES = new Identifier("textures/misc/enchanted_item_glint.png");
        }

        // Retrieve the player handler from playerRenderState
        PlayerHandler playerHandler = PlayerHandler.get(entitylivingbaseIn.getUuid());

        // Redirect to custom buffer if player has cape glint, otherwise use the default buffer
        if (MinecraftCapesConfig.isCapeVisible() && playerHandler.getHasCapeGlint()) {
            minecraftcapes$renderEchantmentGlint(entitylivingbaseIn, partialTicks);
        }
    }

    @Unique
    private void minecraftcapes$renderEchantmentGlint(LivingEntity entitylivingbaseIn, float p_177183_5_) {
        float f = (float)entitylivingbaseIn.ticks + p_177183_5_;
        this.parent.bindTexture(minecraftcapes$ENCHANTED_ITEM_GLINT_RES);
        GlStateManager.enableBlend();
        GlStateManager.depthFunc(514);
        GlStateManager.depthMask(false);
        float f1 = 0.5F;
        GlStateManager.color4f(f1, f1, f1, 1.0F);

        for (int i = 0; i < 2; ++i)
        {
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(768, 1);
            float f2 = 0.76F;
            GlStateManager.color4f(0.5F * f2, 0.25F * f2, 0.8F * f2, 1.0F);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f3 = 0.33333334F;
            GlStateManager.scalef(f3, f3, f3);
            GlStateManager.rotatef(30.0F - (float)i * 60.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translatef(0.0F, f * (0.001F + (float)i * 0.003F) * 20.0F, 0.0F);
            GlStateManager.matrixMode(5888);
            this.parent.getModel().renderCape(0.0625F);
        }

        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
        GlStateManager.disableBlend();
    }

}
