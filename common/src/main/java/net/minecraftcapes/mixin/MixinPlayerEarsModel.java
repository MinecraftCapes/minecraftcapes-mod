package net.minecraftcapes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerEarsModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEarsModel.class)
public abstract class MixinPlayerEarsModel extends HumanoidModel<AvatarRenderState> {

    public MixinPlayerEarsModel(ModelPart modelPart) {
        super(modelPart);
    }
    
    @Inject(method = "createEarsLayer", at = @At(value = "RETURN"), cancellable = true)
    private static void createEarsLayer(CallbackInfoReturnable<LayerDefinition> cir, @Local MeshDefinition meshdefinition, @Local(ordinal = 1) PartDefinition partdefinition1) {
        cir.cancel();
        CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(1.0F, 1.0F, 0.2F));
        partdefinition1.addOrReplaceChild("left_ear", cubelistbuilder, PartPose.offset(-6.0F, -6.0F, 0.0F));
        partdefinition1.addOrReplaceChild("right_ear", cubelistbuilder, PartPose.offset(6.0F, -6.0F, 0.0F));
        cir.setReturnValue(LayerDefinition.create(meshdefinition, 14, 7));
    }

}
