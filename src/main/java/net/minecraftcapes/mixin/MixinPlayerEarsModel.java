package net.minecraftcapes.mixin;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPlayer.class)
public class MixinPlayerEarsModel {

    @Shadow
    private ModelRenderer bipedDeadmau5Head;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void minecraftcapes$createEars(float p_i46304_1_, boolean p_i46304_2_, CallbackInfo ci) {
        ModelPlayer modelPlayer = (ModelPlayer) (Object) this;

        bipedDeadmau5Head = new ModelRenderer(modelPlayer, 0, 0);
        bipedDeadmau5Head.setTextureSize(14, 7);
        //X, Y, Z
        bipedDeadmau5Head.addBox(-8.75F, -9.5F, 0.0F, 6, 6, 1, p_i46304_1_); //Right from back
        bipedDeadmau5Head.addBox(2.75F, -9.5F, 0.0F, 6, 6, 1, p_i46304_1_); //Left from back
    }

}
