package net.minecraftcapes.mixin;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {

    protected MixinLivingEntityRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
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
