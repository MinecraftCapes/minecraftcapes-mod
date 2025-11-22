package net.minecraftcapes.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.Deadmau5HeadLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Deadmau5HeadLayer.class)
public abstract class MixinDeadmau5EarsLayer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

    public MixinDeadmau5EarsLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> p_i50926_1_) {
        super(p_i50926_1_);
    }

    @Inject(method = "render(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;FFFFFFF)V", at = @At("HEAD"), cancellable = true)
    public void render(AbstractClientPlayerEntity var1, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_, CallbackInfo ci) {
        //Cancel default render
        if(!var1.getName().toString().equalsIgnoreCase("deadmau5")) {
            ci.cancel();
        }

        PlayerHandler playerHandler = PlayerHandler.get(var1.getUUID());
        if (playerHandler.getEarLocation() != null && !var1.isInvisible() && MinecraftCapesConfig.isEarsVisible()) {
            this.bindTexture(playerHandler.getEarLocation());

            GlStateManager.pushMatrix();
            float f2 = 1.3333334F;
            GlStateManager.scalef(f2, f2, f2);
            if(var1.isSneaking()) {
                GlStateManager.translatef(0.0F, 0.2F, 0.0F);
            }
            this.getParentModel().renderEars(0.0625F);
            GlStateManager.popMatrix();
        }
    }
}