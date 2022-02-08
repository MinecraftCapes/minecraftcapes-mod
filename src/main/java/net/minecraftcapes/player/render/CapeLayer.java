package net.minecraftcapes.player.render;

import net.minecraft.client.network.AbstractClientPlayerBase;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerBaseModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;

public class CapeLayer extends FeatureRenderer<AbstractClientPlayerBase, PlayerBaseModel<AbstractClientPlayerBase>> {
	
	public CapeLayer(FeatureRendererContext<AbstractClientPlayerBase, PlayerBaseModel<AbstractClientPlayerBase>> featureRendererContext_1) {
		super(featureRendererContext_1);
	}

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, AbstractClientPlayerBase abstractClientPlayerBase, float f, float g, float h, float j, float k, float l) {
		if(!MinecraftCapesConfig.isCapeVisible() && abstractClientPlayerBase.getCapeTexture() == null) return;

		PlayerHandler playerHandler = PlayerHandler.getFromPlayer(abstractClientPlayerBase);
		if(playerHandler.getShowCape()) {
			if (!abstractClientPlayerBase.isInvisible() && (playerHandler.getCapeLocation() != null || abstractClientPlayerBase.getCapeTexture() != null)) {
				ItemStack itemStack = abstractClientPlayerBase.getEquippedStack(EquipmentSlot.CHEST);
				if(itemStack.getItem() != Items.ELYTRA || (playerHandler.getForceHideElytra() && !playerHandler.getForceShowElytra())) {
					matrixStack.push();
					matrixStack.translate(0.0D, 0.0D, 0.125D);
					double d = MathHelper.lerp((double) h, abstractClientPlayerBase.prevCapeX, abstractClientPlayerBase.capeX) - MathHelper.lerp((double) h, abstractClientPlayerBase.prevX, abstractClientPlayerBase.getX());
					double e = MathHelper.lerp((double) h, abstractClientPlayerBase.prevCapeY, abstractClientPlayerBase.capeY) - MathHelper.lerp((double) h, abstractClientPlayerBase.prevY, abstractClientPlayerBase.getY());
					double m = MathHelper.lerp((double) h, abstractClientPlayerBase.prevCapeZ, abstractClientPlayerBase.capeZ) - MathHelper.lerp((double) h, abstractClientPlayerBase.prevZ, abstractClientPlayerBase.getZ());
					float n = abstractClientPlayerBase.prevBodyYaw + (abstractClientPlayerBase.bodyYaw - abstractClientPlayerBase.prevBodyYaw);
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

					float t = MathHelper.lerp(h, abstractClientPlayerBase.prevStrideDistance, abstractClientPlayerBase.strideDistance);
					q += MathHelper.sin(MathHelper.lerp(h, abstractClientPlayerBase.prevHorizontalSpeed, abstractClientPlayerBase.horizontalSpeed) * 6.0F) * 32.0F * t;
					if (abstractClientPlayerBase.isInSneakingPose()) {
						q += 25.0F;
					}

					matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(6.0F + r / 2.0F + q));
					matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(s / 2.0F));
					matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F - s / 2.0F));

					VertexConsumer vertexConsumer;
					if(MinecraftCapesConfig.isCapeVisible() && playerHandler.getCapeLocation() != null) {
						vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(playerHandler.getCapeLocation()), false, playerHandler.getHasCapeGlint());
					} else {
						vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(abstractClientPlayerBase.getCapeTexture()), false, false);
					}

					this.getContextModel().renderCape(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
					matrixStack.pop();
				}
			}
		}
	}
}