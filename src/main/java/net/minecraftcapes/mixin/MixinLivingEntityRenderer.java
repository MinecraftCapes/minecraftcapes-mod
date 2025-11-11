package net.minecraftcapes.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, M extends net.minecraft.client.renderer.entity.model.EntityModel<T>> extends EntityRenderer<T> implements IEntityRenderer<T, M> {

    protected MixinLivingEntityRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Inject(method = "setupRotations", at = @At("TAIL"))
    public void renderUpsidedown(T var1, float var2, float var3, float var4, CallbackInfo ci) {
        if(var1 instanceof PlayerEntity) {
            PlayerHandler playerHandler = PlayerHandler.get(var1.getUUID());
            if(playerHandler.isUpsideDown()) {
                GlStateManager.translatef(0.0F, var1.getBbHeight() + 0.1F, 0.0F);
                GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }
}
