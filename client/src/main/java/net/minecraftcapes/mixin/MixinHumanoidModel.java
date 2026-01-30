package net.minecraftcapes.mixin;

import net.minecraft.client.render.model.ModelPart;
import net.minecraft.client.render.model.entity.HumanoidModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class MixinHumanoidModel {

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

        left_ear.addBox(2.75F, -9.5F, 0.0F, 6, 6, 1, dilation); //Left from back
        right_ear.addBox(-8.75F, -9.5F, 0.0F, 6, 6, 1, dilation); //Right from back
    }

    @Inject(method = "renderDeadmau5Ears", at = @At(value = "HEAD"), cancellable = true)
    public void minecraftcapes$renderDeadmau5Ears(float scale, CallbackInfo ci) {
        this.left_ear.rotationY = this.head.rotationY;
        this.right_ear.rotationY = this.head.rotationY;

        this.left_ear.rotationX = this.head.rotationX;
        this.right_ear.rotationX = this.head.rotationX;

        this.left_ear.render(scale);
        this.right_ear.render(scale);

        ci.cancel();
    }

}
