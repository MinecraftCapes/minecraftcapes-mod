package net.minecraftcapes.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.AbstractArmorLayer;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeLayer.class)
public abstract class MixinCapeLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    @Unique
    private final PlayerModel<AbstractClientPlayer> minecraftCapes$playerModel = this.getParentModel();
    @Unique
    private final EntityModel<AbstractClientPlayer> minecraftcapes$capeModel = new EntityModel<AbstractClientPlayer>() {
        @Override
        public void render(AbstractClientPlayer entity, float f, float g, float h, float i, float j, float k) {
            minecraftCapes$playerModel.renderCloak(0.0625F);
        }
    };

    public MixinCapeLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;pushMatrix()V"))
    public void minecraftcapes$enableBlend(AbstractClientPlayer p_212842_1_, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_, CallbackInfo ci) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }
    
    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;popMatrix()V"))
    public void minecraftcapes$renderGlint(AbstractClientPlayer var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, CallbackInfo ci) {
        PlayerHandler playerHandler = PlayerHandler.get(var1.getUUID());
        if (playerHandler.getHasCapeGlint()) {
            AbstractArmorLayer.renderFoil(this::bindTexture, var1, minecraftcapes$capeModel, var2, var3, var4, var5, var6, var7, var8);
        }
        
        GlStateManager.disableBlend();
    }
}
