package co.uk.minecraftcapes;

import static co.uk.minecraftcapes.reference.Reference.MODID;

import java.util.List;
import java.util.ListIterator;

import co.uk.minecraftcapes.events.PlayerEventHandler;
import co.uk.minecraftcapes.render.Deadmau5;
import co.uk.minecraftcapes.render.LayerCape;
import co.uk.minecraftcapes.render.LayerElytra;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;
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
		Minecraft.getInstance().gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, true);			
		
		for(RenderPlayer render : Minecraft.getInstance().getRenderManager().getSkinMap().values()) { //Get Skin Types
			
			//This removes the Elytra layer from the skinmaps
			try {
				List<LayerRenderer<?>> layerRenderers = ObfuscationReflectionHelper.getPrivateValue(RenderLivingBase.class, render, "field_177097_h");				

				ListIterator<LayerRenderer<?>> it = layerRenderers.listIterator();
				while(it.hasNext()) {					
					if(it.next() instanceof net.minecraft.client.renderer.entity.layers.LayerElytra) {
						it.remove();
					}
				}
				
				ObfuscationReflectionHelper.setPrivateValue(RenderLivingBase.class, render, layerRenderers, "field_177097_h");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			render.addLayer(new LayerCape(render)); //Add Cape to ALL skins
			render.addLayer(new Deadmau5(render)); //Assign ears to ALL skins
			render.addLayer(new LayerElytra(render)); //Assign elyra					
		}
	}	
	
}
