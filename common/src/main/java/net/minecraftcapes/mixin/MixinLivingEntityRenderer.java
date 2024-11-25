package net.minecraftcapes.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftcapes.compatibility.CompatHooks;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> implements RenderLayerParent<S, M> {

	protected MixinLivingEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("RETURN"))
	public void render(S p_361886_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_, CallbackInfo callbackInfo) {
		if(p_361886_ instanceof PlayerRenderState) {
			CompatHooks.getHooks().forEach(hook -> hook.onPlayerRender((PlayerRenderState) p_361886_));
		}
	}

	@Inject(method = "isEntityUpsideDown", at = @At("RETURN"), cancellable = true)
	private static void isEntityUpsideDown(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
		if(livingEntity instanceof Player) {
			Player player = (Player) livingEntity;
			PlayerHandler playerHandler = PlayerHandler.get(player);

			cir.setReturnValue(playerHandler.isUpsideDown() || cir.getReturnValue());
		}
	}
}
