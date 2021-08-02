package net.minecraftcapes.player.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;

public class Deadmau5 extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	public Deadmau5(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
		super(featureRendererContext);
	}

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, float h, float j, float k, float l) {
		if(!MinecraftCapesConfig.isEarsVisible()) return;

		PlayerHandler playerHandler = PlayerHandler.getFromPlayer(abstractClientPlayerEntity);
		if (!abstractClientPlayerEntity.isInvisible() && playerHandler.getEarLocation() != null) {
			VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(playerHandler.getEarLocation()));
			int m = LivingEntityRenderer.getOverlay(abstractClientPlayerEntity, 0.0F);

			matrixStack.push();
			if (abstractClientPlayerEntity.isInSneakingPose()) {
				matrixStack.translate(0.0F, 0.25F, 0.0F);
			}
			matrixStack.scale(1.3333334F, 1.3333334F, 1.3333334F);
			this.getContextModel().renderEars(matrixStack, vertexConsumer, i, m);
			matrixStack.pop();
		}
	}
}