package net.minecraftcapes.mixin;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerDeadmau5Head.class)
public class MixinDeadmau5EarsLayer {

    @Shadow
    private RenderPlayer playerRenderer;

    @Inject(method = "doRenderLayer(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V", at = @At("HEAD"), cancellable = true)
    public void render(AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale, CallbackInfo ci) {
        //Cancel default render
        if(!entitylivingbaseIn.getName().equalsIgnoreCase("deadmau5")) {
            ci.cancel();
        }

        PlayerHandler playerHandler = PlayerHandler.get(entitylivingbaseIn.getUniqueID());
        if (playerHandler.getEarLocation() != null && !entitylivingbaseIn.isInvisible() && MinecraftCapesConfig.isEarsVisible()) {
            this.playerRenderer.bindTexture(playerHandler.getEarLocation());

            GlStateManager.pushMatrix();
            float f2 = 1.3333334F;
            GlStateManager.scale(f2, f2, f2);
            if(entitylivingbaseIn.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            this.playerRenderer.getMainModel().renderDeadmau5Head(0.0625F);
            GlStateManager.popMatrix();
        }
    }

}
