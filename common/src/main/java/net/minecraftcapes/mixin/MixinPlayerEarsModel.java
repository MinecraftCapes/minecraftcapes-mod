package net.minecraftcapes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.model.PlayerEarsModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerEarsModel.class)
public class MixinPlayerEarsModel {

    @Inject(method = "createEarsLayer", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void createEarsLayer(CallbackInfoReturnable<LayerDefinition> cir, MeshDefinition meshdefinition, PartDefinition partdefinition, PartDefinition partdefinition1, CubeListBuilder cubelistbuilder) {
        cir.cancel();
        cubelistbuilder = CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(1.0F, 1.0F, 0.2F));
        partdefinition1.addOrReplaceChild("left_ear", cubelistbuilder, PartPose.offset(-6.0F, -6.0F, 0.0F));
        partdefinition1.addOrReplaceChild("right_ear", cubelistbuilder, PartPose.offset(6.0F, -6.0F, 0.0F));
        cir.setReturnValue(LayerDefinition.create(meshdefinition, 14, 7));
    }
}
