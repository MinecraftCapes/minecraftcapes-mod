package net.minecraftcapes.player.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraftcapes.player.PlayerHandler;

public class ElytraLayer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	private static final Identifier SKIN = new Identifier("textures/entity/elytra.png");
	private final ElytraEntityModel<AbstractClientPlayerEntity> elytra = new ElytraEntityModel<AbstractClientPlayerEntity>();

	public ElytraLayer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext_1) {
		super(featureRendererContext_1);
	}

	public void render(AbstractClientPlayerEntity livingEntity, float f, float g, float h, float i, float j, float k, float l) {
		PlayerHandler playerHandler = PlayerHandler.getFromPlayer(livingEntity);
		ItemStack itemStack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);
		if((itemStack.getItem() == Items.ELYTRA || playerHandler.getForceShowElytra()) && !playerHandler.getForceHideElytra()) {
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

			if (playerHandler.getCapeLocation() != null && livingEntity.isPartVisible(PlayerModelPart.CAPE)) {
				this.bindTexture(playerHandler.getCapeLocation());
			} else {
				this.bindTexture(SKIN);
			}

			GlStateManager.pushMatrix();
			GlStateManager.translatef(0.0F, 0.0F, 0.125F);
			this.elytra.setAngles(livingEntity, f, g, i, j, k, l);
			this.elytra.render(livingEntity, f, g, i, j, k, l);
			if (itemStack.hasEnchantments()) {
				ArmorFeatureRenderer.renderEnchantedGlint(this::bindTexture, livingEntity, this.elytra, f, g, h, i, j, k, l);
			}

			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean hasHurtOverlay() {
		return false;
	}
}
