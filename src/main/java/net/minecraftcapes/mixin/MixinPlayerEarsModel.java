package net.minecraftcapes.mixin;

import net.minecraft.client.render.model.ModelPart;
import net.minecraft.client.render.model.entity.PlayerModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public class MixinPlayerEarsModel {

    @Shadow
    private ModelPart ears;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void minecraftcapes$createEars(float p_i46304_1_, boolean p_i46304_2_, CallbackInfo ci) {
        PlayerModel modelPlayer = (PlayerModel) (Object) this;

        ears = new ModelPart(modelPlayer, 0, 0);
        ears.setTextureSize(14, 7);
        //X, Y, Z
        ears.addBox(-8.75F, -9.5F, 0.0F, 6, 6, 1, p_i46304_1_); //Right from back
        ears.addBox(2.75F, -9.5F, 0.0F, 6, 6, 1, p_i46304_1_); //Left from back
    }

}
