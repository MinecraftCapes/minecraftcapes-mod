package net.minecraftcapes.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftcapes.compatibility.CompatHooks;
import net.minecraftcapes.player.render.CapeLayer;
import net.minecraftcapes.player.render.Deadmau5;
import net.minecraftcapes.player.render.ElytraLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerEntityRenderer extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    
    public MixinPlayerEntityRenderer(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> entityModel, float f) {
        super(context, entityModel, f);
    }
    
    @Inject(method = "<init>*", at = @At("RETURN"))
	private void construct(EntityRendererProvider.Context ctm, boolean alex, CallbackInfo info){
        addLayer(new CapeLayer(this));
        addLayer(new Deadmau5(this));
        addLayer(new ElytraLayer(this, ctm.getModelSet()));

		layers.removeIf(modelFeature -> modelFeature instanceof net.minecraft.client.renderer.entity.layers.ElytraLayer);
        layers.removeIf(modelFeature -> modelFeature instanceof net.minecraft.client.renderer.entity.layers.CapeLayer);
	}

	@Inject(method = "render", at = @At("RETURN"))
	private void render(AbstractClientPlayer abstractClientPlayer, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
		CompatHooks.getHooks().forEach(hook -> hook.onPlayerRender(abstractClientPlayer));
	}

}
