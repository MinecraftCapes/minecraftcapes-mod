package net.minecraftcapes.player.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

public class CapeEntityModel <T extends LivingEntity> extends EntityModel<T> {

    private ModelPart cape;

    public CapeEntityModel() {
        this.cape = new ModelPart(this, 0, 0);
        this.cape.setTextureSize(64, 32);
        this.cape.addCuboid(-5.0F, 0.0F, -1.0F, 10, 16, 1);
    }

    public void render(T livingEntity, float f, float g, float h, float i, float j, float k) {
        this.cape.render(k);
    }

    public void setAngles(T livingEntity, float f, float g, float h, float i, float j, float k) {
        super.setAngles(livingEntity, f, g, h, i, j, k);
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