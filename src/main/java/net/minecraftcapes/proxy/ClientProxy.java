package net.minecraftcapes.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.events.KeyHandlerEvent;
import net.minecraftcapes.events.PlayerEventHandler;
import net.minecraftcapes.player.render.CapeLayer;
import net.minecraftcapes.player.render.Deadmau5;
import net.minecraftcapes.player.render.ElytraLayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.ListIterator;

public class ClientProxy implements IProxy {

    public static final KeyBinding menuKey = new KeyBinding("key.minecraftcapes.gui", Keyboard.KEY_J, "category.minecraftcapes.gui");

    @Override
    public void init() {
        //Prep the config
        MinecraftCapesConfig.loadConfig();

        //Register the keybinds
        ClientRegistry.registerKeyBinding(menuKey);
    }

    @Override
    public void postInit() {
        MinecraftForge.EVENT_BUS.register(new KeyHandlerEvent());
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());

        Minecraft.getMinecraft().gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, true);

        for(RenderPlayer render : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) { //Get Skin Types

            try {
                //This removes the Elytra layer from the skinmaps
                List<LayerRenderer<?>> layerRenderers = ObfuscationReflectionHelper.getPrivateValue(RenderLivingBase.class, render, "field_177097_h");
                ListIterator<LayerRenderer<?>> it = layerRenderers.listIterator();
                while(it.hasNext()) {
                    LayerRenderer<?> value = it.next();
                    if(value instanceof net.minecraft.client.renderer.entity.layers.LayerElytra) {
                        it.remove();
                    }

                    if(value instanceof net.minecraft.client.renderer.entity.layers.LayerCape) {
                        it.remove();
                    }
                }
                ObfuscationReflectionHelper.setPrivateValue(RenderLivingBase.class, render, layerRenderers, "field_177097_h");

                //This makes deadmau5 ears look better when crouching/gliding/swimming
                ModelRenderer bipedDeadmau5Head = new ModelRenderer(render.getMainModel(), 0, 0);
                bipedDeadmau5Head.setTextureSize(14, 7);
                bipedDeadmau5Head.addBox(1.5F, -10.5F, -1.0F, 6, 6, 1, 0.0F);
                bipedDeadmau5Head.addBox(-7.5F, -10.5F, -1.0F, 6, 6, 1, 0.0F);
                bipedDeadmau5Head.setRotationPoint(0.0F, 0.0F, 0.0F);
                ObfuscationReflectionHelper.setPrivateValue(ModelPlayer.class, render.getMainModel(), bipedDeadmau5Head, "field_178736_x");
            } catch (Exception e) {
                e.printStackTrace();
            }

            render.addLayer(new CapeLayer(render)); //Add Cape to ALL skins
            render.addLayer(new Deadmau5(render)); //Assign ears to ALL skins
            render.addLayer(new ElytraLayer(render)); //Assign Elytra
        }
    }
}
