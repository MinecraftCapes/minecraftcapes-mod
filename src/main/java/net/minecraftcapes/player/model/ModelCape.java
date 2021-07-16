package net.minecraftcapes.player.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class ModelCape<T extends LivingEntity> extends EntityModel<T> {

    private RendererModel cape;

    public ModelCape() {
        this.cape = new RendererModel(this, 0, 0);
        this.cape.setTextureSize(64, 32);
        this.cape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1);
    }

    @Override
    public void render(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.cape.render(scale);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        PlayerEntity livingEntity = (PlayerEntity) entityIn;
        if (livingEntity.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty()) {
            if (livingEntity.shouldRenderSneaking()) {
                this.cape.rotationPointZ = 1.4F;
                this.cape.rotationPointY = 1.85F;
            } else {
                this.cape.rotationPointZ = 0.0F;
                this.cape.rotationPointY = 0.0F;
            }
        } else if (livingEntity.shouldRenderSneaking()) {
            this.cape.rotationPointZ = 0.3F;
            this.cape.rotationPointY = 0.8F;
        } else {
            this.cape.rotationPointZ = -1.1F;
            this.cape.rotationPointY = -0.85F;
        }
    }
}