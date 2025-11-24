package net.minecraftcapes.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public class MixinBipedEntityModel {

    @Shadow
    public ModelPart head;
    @Unique
    private ModelPart left_ear;
    @Unique
    private ModelPart right_ear;

    @Inject(method = "<init>(FF)V", at = @At(value = "RETURN"))
    public void minecraftcapes$fixEars(float dilation, float pivotOffsetY, CallbackInfo ci) {
        left_ear = new ModelPart(24, 0);
        right_ear = new ModelPart(24, 0);

        left_ear.addCuboid(2.75F, -9.5F, 0.0F, 6, 6, 1, dilation); //Left from back
        right_ear.addCuboid(-8.75F, -9.5F, 0.0F, 6, 6, 1, dilation); //Right from back
    }

    @Inject(method = "renderEars", at = @At(value = "HEAD"), cancellable = true)
    public void minecraftcapes$renderEars(float scale, CallbackInfo ci) {
        this.left_ear.yaw = this.head.yaw;
        this.right_ear.yaw = this.head.yaw;

        this.left_ear.pitch = this.head.pitch;
        this.right_ear.pitch = this.head.pitch;

        this.left_ear.render(scale);
        this.right_ear.render(scale);

        ci.cancel();
    }

}
