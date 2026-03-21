package net.minecraftcapes.mixin.vanilla;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.player.PlayerEarsModel;
import net.minecraft.client.model.player.PlayerModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEarsModel.class)
public abstract class MixinPlayerEarsModel extends PlayerModel {
    
    public MixinPlayerEarsModel(ModelPart root, boolean slim) {
        super(root, slim);
    }
    
    @Inject(method = "createEarsLayer", at = @At(value = "RETURN"), cancellable = true)
    private static void minecraftcapes$createEars(CallbackInfoReturnable<LayerDefinition> cir, @Local(name = "mesh") MeshDefinition mesh, @Local(name = "head") PartDefinition head) {
        cir.cancel();
        CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(1.0F, 1.0F, 0.2F));
        head.addOrReplaceChild("left_ear", cubelistbuilder, PartPose.offset(-6.0F, -6.0F, 0.0F));
        head.addOrReplaceChild("right_ear", cubelistbuilder, PartPose.offset(6.0F, -6.0F, 0.0F));
        cir.setReturnValue(LayerDefinition.create(mesh, 14, 7));
    }

}
