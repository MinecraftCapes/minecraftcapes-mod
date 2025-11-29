package net.minecraftcapes.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public class MixinPlayerEarsModel<T extends LivingEntity> extends HumanoidModel<T> {

    @Shadow
    private ModelPart ear;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void minecraftcapes$createEars(float p_i46304_1_, boolean p_i46304_2_, CallbackInfo ci) {
        PlayerModel modelPlayer = (PlayerModel) (Object) this;

        ear = new ModelPart(modelPlayer, 0, 0);
        ear.setTexSize(14, 7);
        //X, Y, Z
        ear.addBox(-8.75F, -9.5F, 0.0F, 6, 6, 1, p_i46304_1_); //Right from back
        ear.addBox(2.75F, -9.5F, 0.0F, 6, 6, 1, p_i46304_1_); //Left from back
    }

}
