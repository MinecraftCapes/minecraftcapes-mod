package net.minecraftcapes.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.Deadmau5EarsLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraftcapes.player.ExtendedRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Deadmau5EarsLayer.class)
public abstract class MixinDeadmau5EarsLayer extends RenderLayer<PlayerRenderState, PlayerModel> {
    
    @Shadow
    @Final
    private HumanoidModel<PlayerRenderState> model;
    
    public MixinDeadmau5EarsLayer(RenderLayerParent<PlayerRenderState, PlayerModel> p_117346_) {
        super(p_117346_);
    }
    
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/PlayerRenderState;FF)V", at = @At("HEAD"), cancellable = true)
    public void render(PoseStack poseStack, MultiBufferSource p_116874_, int p_116875_, PlayerRenderState playerRenderState, float p_116877_, float p_116878_, CallbackInfo ci) {
        //Cancel default render
        if(!playerRenderState.name.equalsIgnoreCase("deadmau5")) {
            ci.cancel();
        }
        
        ExtendedRenderState extendedRenderState = (ExtendedRenderState) playerRenderState;
        
        if (extendedRenderState.minecraftcapes$getEarsTexture() != null && !playerRenderState.isInvisible && extendedRenderState.minecraftcapes$getEarsEnabled()) {
            VertexConsumer vertexconsumer = p_116874_.getBuffer(RenderType.entitySolid(extendedRenderState.minecraftcapes$getEarsTexture()));
            int i = LivingEntityRenderer.getOverlayCoords(playerRenderState, 0.0F);
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.setupAnim(playerRenderState);
            this.model.renderToBuffer(poseStack, vertexconsumer, p_116875_, i);
        }
    }
}
