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
import net.minecraftcapes.player.model.CapeEntityModel;

public class CapeLayer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	private final CapeEntityModel<AbstractClientPlayerEntity> cape = new CapeEntityModel<AbstractClientPlayerEntity>();

	public CapeLayer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
		super(featureRendererContext);
	}

	public void render(AbstractClientPlayerEntity livingEntity, float f, float g, float h, float i, float j, float k, float l) {
		PlayerHandler playerHandler = PlayerHandler.getFromPlayer(livingEntity);
		if(playerHandler.getShowCape()) {
			if (!livingEntity.isInvisible() && livingEntity.isPartVisible(PlayerModelPart.CAPE) && playerHandler.getCapeLocation() != null && livingEntity.getCapeTexture() == null) {
				ItemStack itemStack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);
				if (itemStack.getItem() != Items.ELYTRA) {
					GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

					this.bindTexture(playerHandler.getCapeLocation());

					GlStateManager.pushMatrix();
					GlStateManager.translatef(0.0F, 0.0F, 0.125F);

					double d = MathHelper.lerp((double) h, livingEntity.field_7524, livingEntity.field_7500) - MathHelper.lerp((double) h, livingEntity.prevX, livingEntity.x);
					double e = MathHelper.lerp((double) h, livingEntity.field_7502, livingEntity.field_7521) - MathHelper.lerp((double) h, livingEntity.prevY, livingEntity.y);
					double m = MathHelper.lerp((double) h, livingEntity.field_7522, livingEntity.field_7499) - MathHelper.lerp((double) h, livingEntity.prevZ, livingEntity.z);
					float n = livingEntity.field_6220 + (livingEntity.field_6283 - livingEntity.field_6220);
					double o = (double) MathHelper.sin(n * 0.017453292F);
					double p = (double) (-MathHelper.cos(n * 0.017453292F));
					float q = (float) e * 10.0F;
					q = MathHelper.clamp(q, -6.0F, 32.0F);
					float r = (float) (d * o + m * p) * 100.0F;
					r = MathHelper.clamp(r, 0.0F, 150.0F);
					float s = (float) (d * p - m * o) * 100.0F;
					s = MathHelper.clamp(s, -20.0F, 20.0F);
					if (r < 0.0F) {
						r = 0.0F;
					}

					float t = MathHelper.lerp(h, livingEntity.field_7505, livingEntity.field_7483);
					q += MathHelper.sin(MathHelper.lerp(h, livingEntity.prevHorizontalSpeed, livingEntity.horizontalSpeed) * 6.0F) * 32.0F * t;
					if (livingEntity.isInSneakingPose()) {
						q += 25.0F;
					}

					GlStateManager.rotatef(6.0F + r / 2.0F + q, 1.0F, 0.0F, 0.0F);
					GlStateManager.rotatef(s / 2.0F, 0.0F, 0.0F, 1.0F);
					GlStateManager.rotatef(-s / 2.0F, 0.0F, 1.0F, 0.0F);
					GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);

					this.cape.setAngles(livingEntity, f, g, i, j, k, l);
					this.cape.render(livingEntity, f, g, i, j, k, l);
					if (playerHandler.getHasCapeGlint()) {
						ArmorFeatureRenderer.renderEnchantedGlint(this::bindTexture, livingEntity, this.cape, f, g, h, i, j, k, l);
					}

					GlStateManager.disableBlend();
					GlStateManager.popMatrix();
				}
			}
		}
	}

	@Override
	public boolean hasHurtOverlay() {
		return false;
	}
}