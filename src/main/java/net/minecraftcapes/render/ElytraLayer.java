package net.minecraftcapes.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraftcapes.events.PlayerEventHandler;
import net.minecraftcapes.player.PlayerHandler;

public class ElytraLayer {

	public static class LayerRender extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
		
		private static final Identifier SKIN = new Identifier("textures/entity/elytra.png");
		private final ElytraEntityModel<AbstractClientPlayerEntity> elytra = new ElytraEntityModel<AbstractClientPlayerEntity>();
	
		public LayerRender(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext_1) {
			super(featureRendererContext_1);
		}

		public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, AbstractClientPlayerEntity livingEntity, float f, float g, float h, float j, float k, float l) {
			ItemStack itemStack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);
		    if (itemStack.getItem() == Items.ELYTRA) {
				PlayerHandler playerHandler = PlayerHandler.getFromPlayer(livingEntity);
				Identifier capeLocation = playerHandler.getCapeLocation();
				Identifier resourcelocation;
				if (capeLocation != null && livingEntity.isPartVisible(PlayerModelPart.CAPE)) {
					resourcelocation = capeLocation;
		        } else {
					resourcelocation = SKIN;
		        }

		        matrixStack.push();
		        matrixStack.translate(0.0D, 0.0D, 0.125D);
		        this.getContextModel().copyStateTo(this.elytra);
		        this.elytra.setAngles(livingEntity, f, g, j, k, l);
		        VertexConsumer vertexConsumer = ItemRenderer.getArmorVertexConsumer(vertexConsumerProvider, this.elytra.getLayer(resourcelocation), false, itemStack.hasEnchantmentGlint());
		        this.elytra.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		        matrixStack.pop();
		    }
		}
	}
}
