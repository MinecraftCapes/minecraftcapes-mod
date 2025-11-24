package net.minecraftcapes.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
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

    protected MixinLivingEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    @Inject(method = "setupRotations", at = @At(value = "INVOKE", target = "Lnet/minecraft/ChatFormatting;stripFormatting(Ljava/lang/String;)Ljava/lang/String;"))
    public void renderUpsidedown(T var1, float var2, float var3, float var4, CallbackInfo ci) {
        if(var1 instanceof Player) {
            PlayerHandler playerHandler = PlayerHandler.get(var1.getUUID());
            if(playerHandler.isUpsideDown()) {
                GlStateManager.translatef(0.0F, var1.getBbHeight() + 0.1F, 0.0F);
                GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }
}
