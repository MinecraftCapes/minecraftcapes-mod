package net.minecraftcapes.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
public abstract class MixinPlayerModel extends BipedEntityModel {

    @Shadow private ModelPart ears;
    @Shadow private ModelPart cape;

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
        this.ears = bipedDeadmau5Head;
    }

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("RETURN"))
    private void construct(LivingEntity livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        if (livingEntity.getEquippedStack(EquipmentSlot.CHEST).isEmpty()) {
            if (livingEntity.isInSneakingPose()) {
                this.cape.pivotZ = 1.4F;
                this.cape.pivotY = 1.85F;
            } else {
                this.cape.pivotZ = 0.0F;
                this.cape.pivotY = 0.0F;
            }
        } else if (livingEntity.isInSneakingPose()) {
            this.cape.pivotZ = 0.3F;
            this.cape.pivotY = 0.8F;
        } else {
            this.cape.pivotZ = -1.1F;
            this.cape.pivotY = -0.85F;
        }
    }
}
