package net.minecraftcapes.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.Deadmau5EarsLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraftcapes.ExtendedAvatarRenderState;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Deadmau5EarsLayer.class)
public abstract class MixinDeadmau5EarsLayer extends RenderLayer<AvatarRenderState, PlayerModel>{

    @Shadow @Final private HumanoidModel<AvatarRenderState> model;

    public MixinDeadmau5EarsLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer) {
        super(renderer);
    }

    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V", at = @At("HEAD"), cancellable = true)
    public void render(PoseStack p_433932_, SubmitNodeCollector p_435533_, int p_434365_, AvatarRenderState avatarRenderState, float p_435134_, float p_435800_, CallbackInfo ci) {
        PlayerHandler playerHandler = ((ExtendedAvatarRenderState) avatarRenderState).getMinecraftCapes$playerHandler();

        if (playerHandler != null && playerHandler.getEarLocation() != null && !avatarRenderState.isInvisible && MinecraftCapesConfig.isEarsVisible()) {
            int i = LivingEntityRenderer.getOverlayCoords(avatarRenderState, 0.0F);
            p_435533_.submitModel(this.model, avatarRenderState, p_433932_, RenderTypes.entitySolid(playerHandler.getEarLocation()), p_434365_, i, avatarRenderState.outlineColor, null);
        }
    }

}
