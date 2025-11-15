package net.minecraftcapes.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements IEntityRenderer<T, M> {

    protected MixinLivingEntityRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Inject(method = "setupRotations", at = @At("TAIL"))
    public void renderUpsidedown(T livingEntity, MatrixStack poseStack, float p_225621_3_, float p_225621_4_, float p_225621_5_, CallbackInfo ci) {
        if(livingEntity instanceof PlayerEntity) {
            PlayerHandler playerHandler = PlayerHandler.get(livingEntity.getUUID());
            if(playerHandler.isUpsideDown()) {
                poseStack.translate(0.0F, livingEntity.getBbHeight() + 0.1F, 0.0F);
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            }
        }
    }
}
