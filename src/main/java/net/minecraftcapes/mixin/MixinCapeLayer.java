package net.minecraftcapes.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ArmorLayer;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeLayer.class)
public abstract class MixinCapeLayer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

    public MixinCapeLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> p_i50926_1_) {
        super(p_i50926_1_);
    }

    @Unique
    private final PlayerModel<AbstractClientPlayerEntity> minecraftCapes$playerModel = this.getParentModel();
    @Unique
    private final EntityModel<AbstractClientPlayerEntity> minecraftcapes$capeModel = new EntityModel<AbstractClientPlayerEntity>() {
        @Override
        public void render(AbstractClientPlayerEntity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
            minecraftCapes$playerModel.renderCloak(0.0625F);
        }
    };

    @Inject(method = "render(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;pushMatrix()V"))
    public void minecraftcapes$enableBlend(AbstractClientPlayerEntity p_212842_1_, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_, CallbackInfo ci) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }

    @Inject(method = "render(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;popMatrix()V"))
    public void minecraftcapes$renderGlint(AbstractClientPlayerEntity var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, CallbackInfo ci) {
        PlayerHandler playerHandler = PlayerHandler.get(var1.getUUID());
        if (playerHandler.getHasCapeGlint()) {
            ArmorLayer.renderFoil(this::bindTexture, var1, minecraftcapes$capeModel, var2, var3, var4, var5, var6, var7, var8);
        }

        GlStateManager.disableBlend();
    }
}