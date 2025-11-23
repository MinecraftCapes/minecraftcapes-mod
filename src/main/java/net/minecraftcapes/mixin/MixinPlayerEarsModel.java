package net.minecraftcapes.mixin;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public abstract class MixinPlayerEarsModel<T extends LivingEntity> extends BipedModel<T> {

    @Shadow
    private ModelRenderer ear;

    public MixinPlayerEarsModel(float p_i1148_1_) {
        super(p_i1148_1_);
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void createEarsLayer(float p_i46304_1_, boolean p_i46304_2_, CallbackInfo ci) {
        PlayerModel modelPlayer = (PlayerModel) (Object) this;

        ear = new ModelRenderer(modelPlayer, 0, 0);
        ear.setTexSize(14, 7);
        //X, Y, Z
        ear.addBox(-8.75F, -9.5F, 0.0F, 6, 6, 1, p_i46304_1_); //Right from back
        ear.addBox(2.75F, -9.5F, 0.0F, 6, 6, 1, p_i46304_1_); //Left from back
    }

}