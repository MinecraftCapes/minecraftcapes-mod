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
    public MixinDeadmau5EarsLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> p_116860_) {
        super(p_116860_);
    }
    
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        //Cancel default render
        ci.cancel();
        
        PlayerHandler playerHandler = PlayerHandler.getFromPlayer(player);
        if((playerHandler.getEarLocation() != null && !player.isInvisible() && MinecraftCapesConfig.isEarsVisible()) || player.getName().toString().equalsIgnoreCase("deadmau5")) {
            //Check for Deadmau5
            VertexConsumer vertexConsumer = bufferIn.getBuffer(RenderType.entitySolid(playerHandler.getEarLocation()));
            if(player.getName().toString().equalsIgnoreCase("deadmau5")) {
                vertexConsumer = bufferIn.getBuffer(RenderType.entitySolid(player.getSkinTextureLocation()));
            }
            
            int i = LivingEntityRenderer.getOverlayCoords(player, 0.0F);
            
            poseStack.pushPose();
            if(player.isCrouching()) {
                poseStack.translate(0.0F, 0.25F, 0.0F);
            }
            poseStack.scale(1.3333334F, 1.3333334F, 1.3333334F);
            this.getParentModel().renderEars(poseStack, vertexConsumer, packedLightIn, i);
            poseStack.popPose();
        }
    }
    
}
