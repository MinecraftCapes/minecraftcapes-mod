package net.minecraftcapes.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerModel.class)
public abstract class MixinPlayerEarsModel<T extends LivingEntity> extends HumanoidModel<T> {

    public MixinPlayerEarsModel(ModelPart param0) {
        super(param0);
    }
    
    @Inject(method = "createMesh", at = @At(value = "RETURN"), cancellable = true)
    private static void createEarsLayer(CubeDeformation param0, boolean param1, CallbackInfoReturnable<MeshDefinition> cir) {
        MeshDefinition meshDefinition = cir.getReturnValue();
        meshDefinition.getRoot().addOrReplaceChild("ear", CubeListBuilder.create(), PartPose.ZERO);
        
        PartDefinition partDefinition = meshDefinition.getRoot().getChild("ear");
        partDefinition.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(0, 0).addBox(1.5F, -10.5F, -1.0F, 6, 6, 1, param0, 0.21875F, 0.109375F), PartPose.ZERO);
        partDefinition.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(0, 0).addBox(-7.5F, -10.5F, -1.0F, 6, 6, 1, param0, 0.21875F, 0.109375F), PartPose.ZERO);
        cir.setReturnValue(meshDefinition);
    }

}
