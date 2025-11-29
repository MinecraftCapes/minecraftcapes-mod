package net.minecraftcapes.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
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

    @Inject(method = "setupRotations", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/text/TextFormatting;stripFormatting(Ljava/lang/String;)Ljava/lang/String;"))
    public void minecraftcapes$renderUpsideDown(T entity, MatrixStack matrixStack, float p_225621_3_, float p_225621_4_, float p_225621_5_, CallbackInfo ci) {
        if(entity instanceof PlayerEntity) {
            PlayerHandler playerHandler = PlayerHandler.get(entity.getUUID());
            if(playerHandler.isUpsideDown()) {
                matrixStack.translate(0.0F, entity.getBbHeight() + 0.1F, 0.0F);
                matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            }
        }
    }
}
