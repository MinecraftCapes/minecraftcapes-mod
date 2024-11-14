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
import net.minecraftcapes.ExtendedPlayerRenderState;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Deadmau5EarsLayer.class)
public abstract class MixinDeadmau5EarsLayer extends RenderLayer<PlayerRenderState, PlayerModel> {

    @Shadow @Final private HumanoidModel<PlayerRenderState> model;

    public MixinDeadmau5EarsLayer(RenderLayerParent<PlayerRenderState, PlayerModel> renderer) {
        super(renderer);
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/PlayerRenderState;FF)V", at = @At("HEAD"), cancellable = true)
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int p_116875_, PlayerRenderState player, float par5, float par6, CallbackInfo ci) {
        //Cancel default render
        if(!player.name.equalsIgnoreCase("deadmau5")) {
            ci.cancel();
        }

        PlayerHandler playerHandler = ((ExtendedPlayerRenderState) player).getMinecraftCapes$playerHandler();
        if (playerHandler.getEarLocation() != null && !player.isInvisible && MinecraftCapesConfig.isEarsVisible()) {
            //Set the texture to the correct location
            VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entitySolid(playerHandler.getEarLocation()));

            int i = LivingEntityRenderer.getOverlayCoords(player, 0.0F);
            ((PlayerModel)this.getParentModel()).copyPropertiesTo(this.model);
            this.model.setupAnim(player);
            this.model.renderToBuffer(poseStack, vertexconsumer, p_116875_, i);
        }
    }

}
