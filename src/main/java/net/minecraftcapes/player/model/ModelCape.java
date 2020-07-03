package net.minecraftcapes.player.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class ModelCape<T extends LivingEntity> extends AgeableModel<T> {

    private ModelRenderer cape;

    public ModelCape() {
        this.cape = new ModelRenderer(this, 0, 0);
        this.cape.setTextureSize(64, 32);
        this.cape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        PlayerEntity livingEntity = (PlayerEntity) entityIn;
        if (livingEntity.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty()) {
            if (livingEntity.isCrouching()) {
                this.cape.rotationPointZ = 1.4F;
                this.cape.rotationPointY = 1.85F;
            } else {
                this.cape.rotationPointZ = 0.0F;
                this.cape.rotationPointY = 0.0F;
            }
        } else if (livingEntity.isCrouching()) {
            this.cape.rotationPointZ = 0.3F;
            this.cape.rotationPointY = 0.8F;
        } else {
            this.cape.rotationPointZ = -1.1F;
            this.cape.rotationPointY = -0.85F;
        }
    }

    protected Iterable<ModelRenderer> getHeadParts() {
        return ImmutableList.of();
    }

    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of(this.cape);
    }
}
