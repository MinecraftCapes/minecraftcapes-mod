package net.minecraftcapes.mixin;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinDinnerbone extends EntityRenderer {

    protected MixinDinnerbone(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Inject(method = "setupTransforms(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V", at = @At("RETURN"))
    private void construct(LivingEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, CallbackInfo callbackInfo) {
        if(entity instanceof PlayerEntity) {
            if(PlayerHandler.getFromPlayer((PlayerEntity) entity).isUpsideDown()) {
                matrices.translate(0.0D, (double) (entity.getHeight() + 0.1F), 0.0D);
                matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
            }
        }
    }
}