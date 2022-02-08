package net.minecraftcapes.mixin;

import net.minecraft.client.network.AbstractClientPlayerBase;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerBaseRenderer;
import net.minecraft.client.render.entity.model.PlayerBaseModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraftcapes.compatibility.CompatHooks;
import net.minecraftcapes.player.render.CapeLayer;
import net.minecraftcapes.player.render.Deadmau5;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerBaseRenderer.class)
public abstract class MixinPlayerBaseRenderer extends LivingEntityRenderer<AbstractClientPlayerBase, PlayerBaseModel<AbstractClientPlayerBase>> {

	public MixinPlayerBaseRenderer(EntityRendererFactory.Context ctx, PlayerBaseModel<AbstractClientPlayerBase> model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}

	@Inject(method = "<init>*", at = @At("RETURN"))
	private void construct(EntityRendererFactory.Context ctm, boolean alex, CallbackInfo info){
		addFeature(new CapeLayer(this));
		addFeature(new Deadmau5(this));
		addFeature(new ElytraLayer(this, ctm.getModelLoader()));

		features.removeIf(modelFeature -> modelFeature instanceof net.minecraft.client.render.entity.feature.ElytraFeatureRenderer);
		features.removeIf(modelFeature -> modelFeature instanceof net.minecraft.client.render.entity.feature.CapeFeatureRenderer);
	}

	@Inject(method = "render", at = @At("RETURN"))
	private void render(AbstractClientPlayerBase abstractClientPlayerBase, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		CompatHooks.getHooks().forEach(hook -> {
			hook.onPlayerRender(abstractClientPlayerBase);
		});
	}

}
