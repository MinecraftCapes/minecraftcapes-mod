package net.minecraftcapes.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
public abstract class MixinPlayerModel extends BipedEntityModel {

    @Shadow
    private ModelPart ear;

    public MixinPlayerModel(float scale) {
        super(scale);
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void construct(float scale, boolean thinArms, CallbackInfo info) {
        ModelPart bipedDeadmau5Head = new ModelPart(this, 0, 0);
        bipedDeadmau5Head.setTextureSize(14, 7);
        bipedDeadmau5Head.addCuboid(1.5F, -10.5F, -1.0F, 6, 6, 1, 0.0F);
        bipedDeadmau5Head.addCuboid(-7.5F, -10.5F, -1.0F, 6, 6, 1, 0.0F);
        bipedDeadmau5Head.setPivot(0.0F, 0.0F, 0.0F);
        this.ear = bipedDeadmau5Head;
    }
}
