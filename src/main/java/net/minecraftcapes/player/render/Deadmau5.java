package net.minecraftcapes.player.render;

import net.minecraft.client.network.AbstractClientPlayerBase;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerBaseModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;

public class Deadmau5 extends FeatureRenderer<AbstractClientPlayerBase, PlayerBaseModel<AbstractClientPlayerBase>> {

	public Deadmau5(FeatureRendererContext<AbstractClientPlayerBase, PlayerBaseModel<AbstractClientPlayerBase>> featureRendererContext) {
		super(featureRendererContext);
	}

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, AbstractClientPlayerBase abstractClientPlayerBase, float f, float g, float h, float j, float k, float l) {
		if(!MinecraftCapesConfig.isEarsVisible()) return;

		PlayerHandler playerHandler = PlayerHandler.getFromPlayer(abstractClientPlayerBase);
		if (!abstractClientPlayerBase.isInvisible() && playerHandler.getEarLocation() != null) {
			VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(playerHandler.getEarLocation()));
			int m = LivingEntityRenderer.getOverlay(abstractClientPlayerBase, 0.0F);

			matrixStack.push();
			if (abstractClientPlayerBase.isInSneakingPose()) {
				matrixStack.translate(0.0F, 0.25F, 0.0F);
			}
			matrixStack.scale(1.3333334F, 1.3333334F, 1.3333334F);
			this.getContextModel().renderEars(matrixStack, vertexConsumer, i, m);
			matrixStack.pop();
		}
	}
}