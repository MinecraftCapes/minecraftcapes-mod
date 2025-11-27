package net.minecraftcapes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerEarsModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEarsModel.class)
public abstract class MixinPlayerEarsModel extends HumanoidModel<PlayerRenderState> {

    public MixinPlayerEarsModel(ModelPart param0) {
        super(param0);
    }
    
    @Inject(method = "createEarsLayer", at = @At(value = "RETURN"), cancellable = true)
    private static void createEarsLayer(CallbackInfoReturnable<LayerDefinition> cir, @Local MeshDefinition meshdefinition) {
        meshdefinition.getRoot().addOrReplaceChild("ear", CubeListBuilder.create(), PartPose.ZERO);

        PartDefinition partDefinition = meshdefinition.getRoot().getChild("ear");
        partDefinition.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(0, 0).addBox(1.5F, -10.5F, -1.0F, 6, 6, 1, param0, 0.21875F, 0.109375F), PartPose.ZERO);
        partDefinition.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(0, 0).addBox(-7.5F, -10.5F, -1.0F, 6, 6, 1, param0, 0.21875F, 0.109375F), PartPose.ZERO);
        cir.setReturnValue(LayerDefinition.create(meshdefinition, 14, 7));
    }

}
