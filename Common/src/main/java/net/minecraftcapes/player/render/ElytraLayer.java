package net.minecraftcapes.player.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;

public class ElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    
    private static final ResourceLocation WINGS_LOCATION = new ResourceLocation("textures/entity/elytra.png");
    private final ElytraModel<T> elytraModel;
    
    public ElytraLayer(EntityRenderer<? extends Player> p_174493_, EntityModelSet entityModelSet) {
        super((RenderLayerParent<T, M>) p_174493_);
        this.elytraModel = new ElytraModel<>(entityModelSet.bakeLayer(ModelLayers.ELYTRA));
    }

	public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer) livingEntity;
        PlayerHandler playerHandler = PlayerHandler.getFromPlayer(abstractClientPlayer);
		ItemStack itemStack = abstractClientPlayer.getItemBySlot(EquipmentSlot.CHEST);
        if ((itemStack.getItem() == Items.ELYTRA || playerHandler.getForceShowElytra()) && !playerHandler.getForceHideElytra()) {
			ResourceLocation resourcelocation;
			if (playerHandler.getCapeLocation() != null && MinecraftCapesConfig.isCapeVisible()) {
				resourcelocation = playerHandler.getCapeLocation();
			} else if(abstractClientPlayer.getCloakTextureLocation() != null && abstractClientPlayer.isModelPartShown(PlayerModelPart.CAPE)) {
				resourcelocation = abstractClientPlayer.getCloakTextureLocation();
			} else {
				resourcelocation = WINGS_LOCATION;
			}
    
            poseStack.pushPose();
            poseStack.translate(0.0D, 0.0D, 0.125D);
			this.getParentModel().copyPropertiesTo(this.elytraModel);
			this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(bufferIn, this.elytraModel.renderType(resourcelocation), false, itemStack.hasFoil());
			this.elytraModel.renderToBuffer(poseStack, vertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
		}
	}
}