package net.minecraftcapes.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftcapes.MinecraftCapesConstants;
import net.minecraftcapes.player.render.CapeLayer;
import net.minecraftcapes.player.render.Deadmau5;
import net.minecraftcapes.player.render.ElytraLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.util.List;

@Mod.EventBusSubscriber(modid = MinecraftCapesConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AddLayersEvent {

    @SubscribeEvent
    public static void construct(ParticleFactoryRegisterEvent event) {
        ((ReloadableResourceManager)Minecraft.getInstance().getResourceManager()).registerReloadListener((ResourceManagerReloadListener) p_10758_ -> {
            MinecraftCapesConstants.LOG.info("Adding/removing layers to player skin maps...");
            Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().forEach((string, render) -> {
                PlayerRenderer playerRenderer = (PlayerRenderer) render;

                try {
                    //Can we get the entity model set easier than refelection?
                    EntityModelSet entityModelSet = ObfuscationReflectionHelper.getPrivateValue(EntityRenderDispatcher.class, Minecraft.getInstance().getEntityRenderDispatcher(), "f_173996_");
                    playerRenderer.addLayer(new CapeLayer(render)); //Add Cape to ALL skins
                    playerRenderer.addLayer(new Deadmau5(render)); //Assign ears to ALL skins
                    playerRenderer.addLayer(new ElytraLayer<>(render, entityModelSet)); //Assign Elytra

                    //This removes the Cape and Elytra layer from the skinmaps
                    List<RenderLayer<?, ?>> layerRenderers = ObfuscationReflectionHelper.getPrivateValue(LivingEntityRenderer.class, playerRenderer, "f_115291_");
                    layerRenderers.removeIf(modelFeature -> modelFeature instanceof net.minecraft.client.renderer.entity.layers.ElytraLayer);
                    layerRenderers.removeIf(modelFeature -> modelFeature instanceof net.minecraft.client.renderer.entity.layers.CapeLayer);
                    ObfuscationReflectionHelper.setPrivateValue(LivingEntityRenderer.class, playerRenderer, layerRenderers, "f_115291_");

                    //This makes deadmau5 ears look better when crouching/gliding/swimming
                    MeshDefinition meshdefinition = new MeshDefinition();
                    PartDefinition partdefinition = meshdefinition.getRoot();
                    partdefinition.addOrReplaceChild("left_ear", CubeListBuilder.create().addBox(1.5F, -10.5F, -1.0F, 6.0F, 6.0F, 1.0F, CubeDeformation.NONE), PartPose.ZERO);
                    partdefinition.addOrReplaceChild("right_ear", CubeListBuilder.create().addBox(-7.5F, -10.5F, -1.0F, 6.0F, 6.0F, 1.0F, CubeDeformation.NONE), PartPose.ZERO);
                    ModelPart deadMau5Head = partdefinition.bake(14, 7);
                    ObfuscationReflectionHelper.setPrivateValue(PlayerModel.class, playerRenderer.getModel(), deadMau5Head, "f_103379_");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            MinecraftCapesConstants.LOG.info("Done!");
        });
    }
}
