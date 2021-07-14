package net.minecraftcapes.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraftcapes.compatibility.CaelusHook;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.events.KeyHandlerEvent;
import net.minecraftcapes.events.PlayerEventHandler;
import net.minecraftcapes.events.PlayerRenderEvent;
import net.minecraftcapes.gui.MenuScreen;
import net.minecraftcapes.player.render.CapeLayer;
import net.minecraftcapes.player.render.Deadmau5;
import net.minecraftcapes.player.render.ElytraLayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;

public class ClientProxy implements IProxy {

    public static final KeyBinding menuKey = new KeyBinding("key.minecraftcapes.gui", 74, "category.minecraftcapes.gui");

    @Override
    public void init() {
        //Loading Config
        MinecraftCapesConfig.loadConfig();

        //Do Mod compatibility checks :(
        if(doesClassExist("top.theillusivec4.caelus.api.CaelusApi")) {
            new CaelusHook();
        }

        //Register the events
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerRenderEvent());
        MinecraftForge.EVENT_BUS.register(new KeyHandlerEvent());
    }

    @Override
    public void setup() {
        //Register the menu
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> new MenuScreen());

        //Register the keybinds
        ClientRegistry.registerKeyBinding(menuKey);
    }

    @Override
    public void enqueueIMC(){
        Minecraft.getInstance().gameSettings.setModelPartEnabled(PlayerModelPart.CAPE, true);

        for(PlayerRenderer render : Minecraft.getInstance().getRenderManager().getSkinMap().values()) { //Get Skin Types

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
        }
    }

    /**
     * Checks if a class exists or not
     * @param name
     * @return
     */
    private boolean doesClassExist(String name) {
        try {
            Class c = Class.forName(name);
            System.out.println(c);
            if (c != null) {
                return true;
            }
        } catch (ClassNotFoundException e) {}
        return false;
    }
}
