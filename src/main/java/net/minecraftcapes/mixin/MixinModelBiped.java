package net.minecraftcapes.mixin;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBiped.class)
public class MixinModelBiped {

    @Shadow
    private ModelRenderer bipedEars;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void createEarsLayer(CallbackInfo ci) {
        ModelBiped modelBiped = (ModelBiped) (Object) this;

        bipedEars = new ModelRenderer(modelBiped, 0, 0);
        bipedEars.setTextureSize(14, 7);
        //X, Y, Z
        bipedEars.addBox(-8.75F, -9.5F, 0.0F, 6, 6, 1, 0); //Right from back
        bipedEars.addBox(2.75F, -9.5F, 0.0F, 6, 6, 1, 0); //Left from back
    }

}
