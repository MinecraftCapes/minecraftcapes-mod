package net.minecraftcapes.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.player.render.CapeLayer;
import net.minecraftcapes.player.render.Deadmau5;
import net.minecraftcapes.player.render.ElytraLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;

@Mod.EventBusSubscriber(modid = MinecraftCapes.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AddLayersEvent {

    @SubscribeEvent
    public static void construct(ParticleFactoryRegisterEvent event) {
        //Lets see if we can do renders here
        Minecraft.getInstance().getRenderManager().getSkinMap().forEach((type, render) -> {
            try {
                //This removes the Elytra layer from the skinmaps
                List<LayerRenderer<?, ?>> layerRenderers = ObfuscationReflectionHelper.getPrivateValue(LivingRenderer.class, render, "field_177097_h");
                layerRenderers.removeIf(modelFeature -> modelFeature instanceof net.minecraft.client.renderer.entity.layers.ElytraLayer);
                layerRenderers.removeIf(modelFeature -> modelFeature instanceof net.minecraft.client.renderer.entity.layers.CapeLayer);
                ObfuscationReflectionHelper.setPrivateValue(LivingRenderer.class, render, layerRenderers, "field_177097_h");

                //This makes deadmau5 ears look better when crouching/gliding/swimming
                ModelRenderer bipedDeadmau5Head = new ModelRenderer(render.getEntityModel(), 0, 0);
                bipedDeadmau5Head.setTextureSize(14, 7);
                bipedDeadmau5Head.addBox(1.5F, -10.5F, -1.0F, 6, 6, 1, 0.0F);
                bipedDeadmau5Head.addBox(-7.5F, -10.5F, -1.0F, 6, 6, 1, 0.0F);
                bipedDeadmau5Head.setRotationPoint(0.0F, 0.0F, 0.0F);
                ObfuscationReflectionHelper.setPrivateValue(PlayerModel.class, render.getEntityModel(), bipedDeadmau5Head, "field_178736_x");
            } catch (Exception e) {
                e.printStackTrace();
            }

            render.addLayer(new CapeLayer(render)); //Add Cape to ALL skins
            render.addLayer(new Deadmau5(render)); //Assign ears to ALL skins
            render.addLayer(new ElytraLayer<>(render)); //Assign Elytra
        });
    }

}
