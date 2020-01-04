package co.uk.minecraftcapes.events;

import co.uk.minecraftcapes.render.CapeLayer;
import co.uk.minecraftcapes.render.Deadmau5;
import co.uk.minecraftcapes.render.ElytraLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;
import java.util.ListIterator;

public class EnqueueIMCEvent {
    public static void init(){
        Minecraft.getInstance().gameSettings.setModelPartEnabled(PlayerModelPart.CAPE, true);

        for(PlayerRenderer render : Minecraft.getInstance().getRenderManager().getSkinMap().values()) { //Get Skin Types

            //This removes the Elytra layer from the skinmaps
            try {
                List<LayerRenderer<?, ?>> layerRenderers = ObfuscationReflectionHelper.getPrivateValue(LivingRenderer.class, render, "field_177097_h");

                ListIterator<LayerRenderer<?, ?>> it = layerRenderers.listIterator();
                while(it.hasNext()) {
                    if(it.next() instanceof net.minecraft.client.renderer.entity.layers.ElytraLayer) {
                        it.remove();
                    }
                }

                ObfuscationReflectionHelper.setPrivateValue(LivingRenderer.class, render, layerRenderers, "field_177097_h");
            } catch (Exception e) {
                e.printStackTrace();
            }

            render.addLayer(new CapeLayer(render)); //Add Cape to ALL skins
            render.addLayer(new Deadmau5(render)); //Assign ears to ALL skins
            render.addLayer(new ElytraLayer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>(render)); //Assign elyra
        }
    }
}