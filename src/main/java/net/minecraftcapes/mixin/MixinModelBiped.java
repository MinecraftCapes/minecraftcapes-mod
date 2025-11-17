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
    public ModelRenderer bipedEars;

    @Inject(method = "<init>(FFII)V", at = @At(value = "RETURN"))
    public void createEarsLayer(float p_i1149_1_, float p_i1149_2_, int p_i1149_3_, int p_i1149_4_, CallbackInfo ci) {
        ModelBiped modelBiped = (ModelBiped) (Object) this;

        bipedEars = new ModelRenderer(modelBiped, 0, 0);
        bipedEars.setTextureSize(14, 7);
        //X, Y, Z
        bipedEars.addBox(-8.75F, -9.5F, 0.0F, 6, 6, 1, p_i1149_1_); //Right from back
        bipedEars.addBox(2.75F, -9.5F, 0.0F, 6, 6, 1, p_i1149_1_); //Left from back
    }

}
