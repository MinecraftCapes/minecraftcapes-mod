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
import net.minecraft.util.Identifier;
import net.minecraftcapes.player.PlayerHandler;

public class Deadmau5 {
	
	public static class LayerRender extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	
		public LayerRender(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext_1) {
			super(featureRendererContext_1);
		}
		
		public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, float h, float j, float k, float l) {
			PlayerHandler playerHandler = PlayerHandler.getFromPlayer(abstractClientPlayerEntity);
			Identifier rl = playerHandler.getEarLocation();
			if (!abstractClientPlayerEntity.isInvisible() && rl != null) {
				VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(rl));
		        int m = LivingEntityRenderer.getOverlay(abstractClientPlayerEntity, 0.0F);

				matrixStack.push();
				if(abstractClientPlayerEntity.isSneaking()) {
					matrixStack.translate(0.0F, 0.25F, 0.0F);
				}
				matrixStack.scale(1.3333334F, 1.3333334F, 1.3333334F);
				this.getContextModel().renderEars(matrixStack, vertexConsumer, i, m);
				matrixStack.pop();
			}
		}			
	}
}
