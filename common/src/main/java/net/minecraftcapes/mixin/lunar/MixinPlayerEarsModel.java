package net.minecraftcapes.mixin.lunar;

import net.minecraft.client.model.PlayerEarsModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(PlayerEarsModel.class)
public class MixinPlayerEarsModel {
    
    /**
     * I wouldn't usually recommend an overwrite but it stops Lunar complaining...
     * @return
     */
    @Overwrite
    public static LayerDefinition createEarsLayer() {
        MeshDefinition meshdefinition = PlayerModel.createMesh(CubeDeformation.NONE, false);
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition partdefinition1 = partdefinition.getChild("head");
        CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(1.0F, 1.0F, 0.2F));
        partdefinition1.addOrReplaceChild("left_ear", cubelistbuilder, PartPose.offset(-6.0F, -6.0F, 0.0F));
        partdefinition1.addOrReplaceChild("right_ear", cubelistbuilder, PartPose.offset(6.0F, -6.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 14, 7);
    }
    
}
