package net.minecraftcapes.mixin;

import net.minecraft.client.entity.living.player.ClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.client.render.entity.layer.Deadmau5Layer;
import net.minecraft.client.render.platform.GlStateManager;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Deadmau5Layer.class)
public class MixinDeadmau5EarsLayer {

    @Shadow
    private PlayerRenderer parent;

    @Inject(method = "render(Lnet/minecraft/client/entity/living/player/ClientPlayerEntity;FFFFFFF)V", at = @At("HEAD"), cancellable = true)
    public void minecraftcapes$renderEars(ClientPlayerEntity entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale, CallbackInfo ci) {
        PlayerHandler playerHandler = PlayerHandler.get(entitylivingbaseIn.getUuid());

        if (playerHandler.getEarLocation() != null && !entitylivingbaseIn.isInvisible() && MinecraftCapesConfig.isEarsVisible()) {
            this.parent.bindTexture(playerHandler.getEarLocation());

            GlStateManager.pushMatrix();
            float f2 = 1.3333334F;
            GlStateManager.scalef(f2, f2, f2);
            if(entitylivingbaseIn.isSneaking()) {
                GlStateManager.translatef(0.0F, 0.2F, 0.0F);
            }
            this.parent.getModel().renderEars(0.0625F);
            GlStateManager.popMatrix();
        }

        ci.cancel();
    }

}
