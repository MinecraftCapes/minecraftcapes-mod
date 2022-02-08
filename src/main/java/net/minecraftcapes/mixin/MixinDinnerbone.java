package net.minecraftcapes.mixin;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.math.Vec3f;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinDinnerbone extends EntityRenderer {

    protected MixinDinnerbone(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "setupTransforms(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V", at = @At("RETURN"))
    private void construct(LivingEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, CallbackInfo callbackInfo) {
        if(entity instanceof PlayerBase) {
            if(PlayerHandler.getFromPlayer((PlayerBase) entity).isUpsideDown()) {
                matrices.translate(0.0D, (double) (entity.getHeight() + 0.1F), 0.0D);
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
            }
        }
    }
}