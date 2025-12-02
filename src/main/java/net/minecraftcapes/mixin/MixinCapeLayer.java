package net.minecraftcapes.mixin;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerCape.class)
public class MixinCapeLayer {

    @Final
    @Shadow
    private RenderPlayer playerRenderer;

    @Inject(method = "render(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderPlayer;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"))
    public void minecraftcapes$submitCape(AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale, CallbackInfo ci) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(1, 0);
    }

    @Inject(method = "render(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/model/ModelPlayer;renderCape(F)V", shift = At.Shift.AFTER))
    public void minecraftcapes$addCapeGlint(AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale, CallbackInfo ci) {
        // Retrieve the player handler from playerRenderState
        PlayerHandler playerHandler = PlayerHandler.get(entitylivingbaseIn.getUniqueID());

        // Redirect to custom buffer if player has cape glint, otherwise use the default buffer
        if (playerHandler.getHasCapeGlint()) {
            LayerArmorBase.renderEnchantedGlint(playerRenderer, entitylivingbaseIn, new ModelBase() {
                @Override
                public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
                    playerRenderer.getMainModel().renderCape(0.0625F);
                }
            }, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale);
        }

        GlStateManager.disableBlend();
    }
}
