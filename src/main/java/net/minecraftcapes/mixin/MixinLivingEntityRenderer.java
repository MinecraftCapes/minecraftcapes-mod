package net.minecraftcapes.mixin;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.platform.GlStateManager;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity> extends EntityRenderer<T> {

    protected MixinLivingEntityRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Inject(method = "applyRotation", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/living/LivingEntity;getName()Ljava/lang/String;"))
    public void minecraftcapes$renderUpsideDown(T bat, float p_77043_2_, float p_77043_3_, float partialTicks, CallbackInfo ci) {
        if(bat instanceof PlayerEntity) {
            PlayerHandler playerHandler = PlayerHandler.get(bat.getUuid());
            if(playerHandler.isUpsideDown()) {
                GlStateManager.translatef(0.0F, bat.height + 0.1F, 0.0F);
                GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }
}
