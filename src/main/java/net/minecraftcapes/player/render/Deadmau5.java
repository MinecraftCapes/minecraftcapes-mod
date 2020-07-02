package net.minecraftcapes.player.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraftcapes.player.PlayerHandler;

public class Deadmau5 extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	
	public Deadmau5(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext_1) {
		super(featureRendererContext_1);
	}

	@Override
	public void render(AbstractClientPlayerEntity entity, float f, float g, float h, float i, float j, float k, float l) {
		PlayerHandler playerHandler = PlayerHandler.getFromPlayer(entity);
		if (!entity.isInvisible() && playerHandler.getEarLocation() != null) {
			this.bindTexture(playerHandler.getEarLocation());
			GlStateManager.pushMatrix();
			GlStateManager.scalef(1.3333334F, 1.3333334F, 1.3333334F);
			if(entity.isInSneakingPose()) {
				GlStateManager.translatef(0.0F, 0.25F, 0.0F);
			}
			((PlayerEntityModel)this.getContextModel()).renderEars(0.0625F);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean hasHurtOverlay() {
		return false;
	}
}
