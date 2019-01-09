package co.uk.minecraftcapes;

import static co.uk.minecraftcapes.reference.Reference.MODID;

import co.uk.minecraftcapes.events.PlayerEventHandler;
import co.uk.minecraftcapes.render.Deadmau5;
import co.uk.minecraftcapes.render.LayerCape;
import co.uk.minecraftcapes.render.LayerElytra;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.javafmlmod.FMLModLoadingContext;


@Mod(MODID)
public class MinecraftCapes {

	public MinecraftCapes() {		
		FMLModLoadingContext.get().getModEventBus().addListener(this::postInit);

        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
	}
	
	//PostInitialisation
	@SubscribeEvent
	public void postInit(FMLPostInitializationEvent event) {			
		Minecraft.getInstance().gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, true);			
		
		for(RenderPlayer render : Minecraft.getInstance().getRenderManager().getSkinMap().values()) { //Get Skin Types
			render.addLayer(new LayerCape(render)); //Add Cape to ALL skins
			render.addLayer(new Deadmau5(render)); //Assign ears to ALL skins
			render.addLayer(new LayerElytra(render)); //Assign elyra
		}	

		//Adds the default elytra to biped mobs
		for(Render<?> render : Minecraft.getInstance().getRenderManager().entityRenderMap.values()) {
			if(render instanceof RenderBiped) {
				RenderLiving<?> rl = (RenderLiving<?>) render;
				rl.addLayer(new LayerElytra(rl));
			}
		}
	}	
	
}
