package net.minecraftcapes.player.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;

public class ModelCape extends ModelBase {

    private ModelRenderer cape;

    public ModelCape() {
        this.cape = new ModelRenderer(this, 0, 0);
        this.cape.setTextureSize(64, 32);
        this.cape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.cape.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        EntityPlayer livingEntity = (EntityPlayer) entityIn;
        if (livingEntity.getItemStackFromSlot(EntityEquipmentSlot.CHEST) == null) {
            if (livingEntity.isSneaking()) {
                this.cape.rotationPointZ = 1.4F;
                this.cape.rotationPointY = 1.85F;
            } else {
                this.cape.rotationPointZ = 0.0F;
                this.cape.rotationPointY = 0.0F;
            }
        } else if (livingEntity.isSneaking()) {
            this.cape.rotationPointZ = 0.3F;
            this.cape.rotationPointY = 0.8F;
        } else {
            this.cape.rotationPointZ = -1.1F;
            this.cape.rotationPointY = -0.85F;
        }
    }

}
