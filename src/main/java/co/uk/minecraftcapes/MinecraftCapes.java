package co.uk.minecraftcapes;

import static co.uk.minecraftcapes.reference.Reference.MODID;

import co.uk.minecraftcapes.events.PlayerEventHandler;
import co.uk.minecraftcapes.render.CapeLayer;
import co.uk.minecraftcapes.render.Deadmau5;
import co.uk.minecraftcapes.render.ElytraLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
			render.addLayer(new CapeLayer(render)); //Add Cape to ALL skins
			render.addLayer(new Deadmau5(render)); //Assign ears to ALL skins
			render.addLayer(new ElytraLayer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>(render)); //Assign elyra
		}	

		//Adds the default elytra to biped mobs
		for(EntityRenderer<?> render : Minecraft.getInstance().getRenderManager().entityRenderMap.values()) {
			if(render instanceof BipedRenderer) {
				LivingRenderer<?, ?> rl = (LivingRenderer<?, ?>) render;				
				//rl.addLayer(new ElytraLayer<?,?>(rl));
			}
		}
	}	
	
}
