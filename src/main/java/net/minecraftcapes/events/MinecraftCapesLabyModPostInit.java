package net.minecraftcapes.events;

import net.labymod.core.LabyModCore;
import net.labymod.core_implementation.mc18.RenderPlayerImplementation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraftcapes.player.render.CapeLayer;
import net.minecraftcapes.player.render.Deadmau5;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;

public class MinecraftCapesLabyModPostInit {

    public static void init() {
        Minecraft.getMinecraft().gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, true);

        try {
            final Class<?> clazz = LabyModCore.getCoreAdapter().getClass();
            final Field field = clazz.getDeclaredField("renderPlayerImpl");
            field.setAccessible(true);
            field.set(LabyModCore.getCoreAdapter(), new RenderPlayerImplementation() {
                public LayerRenderer<?>[] getLayerRenderers(final RenderPlayer renderPlayer) {
                    LayerRenderer[] layers = super.getLayerRenderers(renderPlayer);
                    layers = ArrayUtils.add(layers, new CapeLayer(renderPlayer));
                    layers = ArrayUtils.add(layers, new Deadmau5(renderPlayer));
                    return layers;
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}