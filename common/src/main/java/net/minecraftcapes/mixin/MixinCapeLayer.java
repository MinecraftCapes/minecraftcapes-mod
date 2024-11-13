package net.minecraftcapes.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.EquipmentModel;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraftcapes.ExtendedPlayerRenderState;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.CapeGlintManager;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeLayer.class)
public abstract class MixinCapeLayer extends RenderLayer<PlayerRenderState, PlayerModel> {

    @Shadow @Final private HumanoidModel<PlayerRenderState> model;

    public MixinCapeLayer(RenderLayerParent<PlayerRenderState, PlayerModel> renderer) {
        super(renderer);
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/PlayerRenderState;FF)V", at = @At("HEAD"), cancellable = true)
    private void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, PlayerRenderState player, float p_116619_, float p_116620_, CallbackInfo ci) {
        //Cancel default render
        ci.cancel();

        //Player skin
        PlayerSkin playerskin = player.skin;

        //Check for cape
        if(!MinecraftCapesConfig.isCapeVisible() && playerskin.capeTexture() == null) return;

        //Get the player handler
        PlayerHandler playerHandler = ((ExtendedPlayerRenderState) player).getPlayerHandler();

        if(playerHandler.getShowCape()) {
            if (!player.isInvisible && (player.skin.capeTexture() != null || playerHandler.getCapeLocation() != null)) {
                if (!this.hasLayer(player.chestItem, EquipmentModel.LayerType.WINGS) || (playerHandler.getForceHideElytra() && !playerHandler.getForceShowElytra())) {
                    poseStack.pushPose();
                    if (this.hasLayer(player.chestItem, EquipmentModel.LayerType.HUMANOID)) {
                        poseStack.translate(0.0F, -0.053125F, 0.06875F);
                    }

                    VertexConsumer vertexconsumer;
                    if (MinecraftCapesConfig.isCapeVisible() && playerHandler.getCapeLocation() != null) {
                        vertexconsumer = CapeGlintManager.getCapeBuffer(bufferIn, RenderType.armorCutoutNoCull(playerHandler.getCapeLocation()), playerHandler.getHasCapeGlint());
                    } else {
                        vertexconsumer = bufferIn.getBuffer(RenderType.entitySolid(playerskin.capeTexture()));
                    }
                    ((PlayerModel) this.getParentModel()).copyPropertiesTo(this.model);
                    this.model.setupAnim(player);
                    this.model.renderToBuffer(poseStack, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY);
                    poseStack.popPose();
                }
            }
        }
    }

    @Shadow
    private boolean hasLayer(ItemStack chestItem, EquipmentModel.LayerType layerType) { return false; }
}
