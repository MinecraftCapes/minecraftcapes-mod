package net.minecraftcapes.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraftcapes.player.render.CapeLayer;
import net.minecraftcapes.player.render.Deadmau5;
import net.minecraftcapes.player.render.ElytraLayer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;
import java.util.ListIterator;

public class EnqueueIMCEvent {
    public static void init(){
        Minecraft.getInstance().gameSettings.setModelPartEnabled(PlayerModelPart.CAPE, true);

        for(PlayerRenderer render : Minecraft.getInstance().getRenderManager().getSkinMap().values()) { //Get Skin Types

            try {
                //This removes the Elytra layer from the skinmaps
                List<LayerRenderer<?, ?>> layerRenderers = ObfuscationReflectionHelper.getPrivateValue(LivingRenderer.class, render, "field_177097_h");
                ListIterator<LayerRenderer<?, ?>> it = layerRenderers.listIterator();
                while(it.hasNext()) {
                    if(it.next() instanceof net.minecraft.client.renderer.entity.layers.ElytraLayer) {
                        it.remove();
                    }
                }
                ObfuscationReflectionHelper.setPrivateValue(LivingRenderer.class, render, layerRenderers, "field_177097_h");

                //This makes deadmau5 ears look better when crouching/gliding/swimming
                RendererModel bipedDeadmau5Head = new RendererModel(render.getEntityModel(), 24, 0);
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
        }
    }
}