package net.minecraftcapes.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {

    protected MixinLivingEntityRenderer(EntityRendererProvider.Context param0) {
        super(param0);
    }

    @Inject(method = "setupRotations", at = @At(value = "INVOKE", target = "Lnet/minecraft/ChatFormatting;stripFormatting(Ljava/lang/String;)Ljava/lang/String;"))
    public void minecraftcapes$renderUpsideDown(T livingEntity, PoseStack poseStack, float f, float g, float h, CallbackInfo ci) {
        if(livingEntity instanceof Player) {
            PlayerHandler playerHandler = PlayerHandler.get(livingEntity.getUUID());
            if(playerHandler.isUpsideDown()) {
                poseStack.translate(0.0F, livingEntity.getBbHeight() + 0.1F, 0.0F);
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            }
        }
    }
}
