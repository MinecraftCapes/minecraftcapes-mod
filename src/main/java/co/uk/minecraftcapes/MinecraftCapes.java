package co.uk.minecraftcapes;

import static co.uk.minecraftcapes.reference.Reference.MODID;

import java.util.List;

import co.uk.minecraftcapes.events.PlayerEventHandler;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MODID)
public class MinecraftCapes {

	public MinecraftCapes() {		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);

        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
	}
	
	//PostInitialisation
	@SubscribeEvent
	public void enqueueIMC(InterModEnqueueEvent event) {			
		Minecraft.getInstance().gameSettings.setModelPartEnabled(PlayerModelPart.CAPE, true);			
		
		for(PlayerRenderer render : Minecraft.getInstance().getRenderManager().getSkinMap().values()) { //Get Skin Types
			
			//This removes the Elytra layer from the skinmaps
			try {
				List<LayerRenderer<?, ?>> layerRenderers = ObfuscationReflectionHelper.getPrivateValue(LivingRenderer.class, render, "layerRenderers");							
				for(LayerRenderer<?, ?> layer : layerRenderers) {
					if(layer instanceof net.minecraft.client.renderer.entity.layers.ElytraLayer) {
						layerRenderers.remove(layer);
					}
				}
				ObfuscationReflectionHelper.setPrivateValue(LivingRenderer.class, render, layerRenderers, "layerRenderers");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			render.addLayer(new CapeLayer(render)); //Add Cape to ALL skins
			render.addLayer(new Deadmau5(render)); //Assign ears to ALL skins
			render.addLayer(new ElytraLayer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>(render)); //Assign elyra					
		}
	}	
	
}
