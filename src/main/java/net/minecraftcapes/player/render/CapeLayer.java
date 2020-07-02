package net.minecraftcapes.player.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraftcapes.player.PlayerHandler;

public class CapeLayer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	private FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext;

	public CapeLayer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
		super(featureRendererContext);
		this.featureRendererContext = featureRendererContext;
	}

	@Override
	public boolean hasHurtOverlay() {
		return false;
	}

	public void render(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, float h, float i, float j, float k, float l) {
		PlayerHandler playerHandler = PlayerHandler.getFromPlayer(abstractClientPlayerEntity);
		if (!abstractClientPlayerEntity.isInvisible() && abstractClientPlayerEntity.isPartVisible(PlayerModelPart.CAPE) && playerHandler.getCapeLocation() != null) {
			ItemStack itemStack = abstractClientPlayerEntity.getEquippedStack(EquipmentSlot.CHEST);
			if (itemStack.getItem() != Items.ELYTRA) {
				GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				this.bindTexture(playerHandler.getCapeLocation());
				GlStateManager.pushMatrix();
				GlStateManager.translatef(0.0F, 0.0F, 0.125F);

				double d = MathHelper.lerp((double)h, abstractClientPlayerEntity.field_7524, abstractClientPlayerEntity.field_7500) - MathHelper.lerp((double)h, abstractClientPlayerEntity.prevX, abstractClientPlayerEntity.x);
				double e = MathHelper.lerp((double)h, abstractClientPlayerEntity.field_7502, abstractClientPlayerEntity.field_7521) - MathHelper.lerp((double)h, abstractClientPlayerEntity.prevY, abstractClientPlayerEntity.y);
				double m = MathHelper.lerp((double)h, abstractClientPlayerEntity.field_7522, abstractClientPlayerEntity.field_7499) - MathHelper.lerp((double)h, abstractClientPlayerEntity.prevZ, abstractClientPlayerEntity.z);
				float n = abstractClientPlayerEntity.field_6220 + (abstractClientPlayerEntity.field_6283 - abstractClientPlayerEntity.field_6220);
				double o = (double)MathHelper.sin(n * 0.017453292F);
				double p = (double)(-MathHelper.cos(n * 0.017453292F));
				float q = (float)e * 10.0F;
				q = MathHelper.clamp(q, -6.0F, 32.0F);
				float r = (float)(d * o + m * p) * 100.0F;
				r = MathHelper.clamp(r, 0.0F, 150.0F);
				float s = (float)(d * p - m * o) * 100.0F;
				s = MathHelper.clamp(s, -20.0F, 20.0F);
				if (r < 0.0F) {
					r = 0.0F;
				}

				float t = MathHelper.lerp(h, abstractClientPlayerEntity.field_7505, abstractClientPlayerEntity.field_7483);
				q += MathHelper.sin(MathHelper.lerp(h, abstractClientPlayerEntity.prevHorizontalSpeed, abstractClientPlayerEntity.horizontalSpeed) * 6.0F) * 32.0F * t;
				if (abstractClientPlayerEntity.isInSneakingPose()) {
					q += 25.0F;
				}

				GlStateManager.rotatef(6.0F + r / 2.0F + q, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotatef(s / 2.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.rotatef(-s / 2.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
				((PlayerEntityModel)this.getContextModel()).renderCape(0.0625F);
				if(playerHandler.getHasCapeGlint()) {
					ArmorFeatureRenderer.renderEnchantedGlint(
							this::bindTexture,
							abstractClientPlayerEntity,
							featureRendererContext.getModel(),
							f, g, h, i, j, k, l);
				}
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			 }
		  }
	}
}