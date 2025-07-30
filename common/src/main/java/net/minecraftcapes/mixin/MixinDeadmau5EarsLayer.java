package net.minecraftcapes.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.Deadmau5EarsLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Deadmau5EarsLayer.class)
public abstract class MixinDeadmau5EarsLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public MixinDeadmau5EarsLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        super(renderer);
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        //Cancel default render
        if(!player.getName().toString().equalsIgnoreCase("deadmau5")) {
            ci.cancel();
        }

        PlayerHandler playerHandler = PlayerHandler.get(player);
        if (playerHandler.getEarLocation() != null && !player.isInvisible() && MinecraftCapesConfig.isEarsVisible()) {
            //Set the texture to the correct location
            VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entitySolid(playerHandler.getEarLocation()));

            int i = LivingEntityRenderer.getOverlayCoords(player, 0.0F);
            this.getParentModel().renderEars(poseStack, vertexconsumer, packedLight, i);
        }
    }

}
