package net.minecraftcapes.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeLayer.class)
public abstract class MixinCapeLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private AbstractClientPlayer abstractClientPlayer;

    public MixinCapeLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> p_i50926_1_) {
        super(p_i50926_1_);
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V", at = @At(value = "HEAD"))
    public void render(PoseStack param0, MultiBufferSource param1, int param2, AbstractClientPlayer abstractClientPlayer, float param4, float param5, float param6, float param7, float param8, float param9, CallbackInfo ci) {
        this.abstractClientPlayer = abstractClientPlayer;
    }

    @Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    public VertexConsumer minecraftcapes$renderGlint(MultiBufferSource instance, RenderType renderType) {
        // Retrieve the player handler from playerRenderState
        PlayerHandler playerHandler = PlayerHandler.get(abstractClientPlayer.getUUID());

        // Redirect to custom buffer if player has cape glint, otherwise use the default buffer
        return ItemRenderer.getFoilBuffer(instance, RenderType.entityCutoutNoCull(abstractClientPlayer.getCloakTextureLocation()), false, playerHandler.getHasCapeGlint());
    }
}
