package co.uk.minecraftcapes.proxy;

import co.uk.minecraftcapes.player.PlayerInfo;
import co.uk.minecraftcapes.player.downloader.DeleteElytra;
import co.uk.minecraftcapes.render.Deadmau5;
import co.uk.minecraftcapes.render.LayerCape;
import co.uk.minecraftcapes.render.LayerElytra;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy implements CommonProxy {
	
	//Calling ONLY on client side
	@Override
	public void postInit() {
		
		MinecraftForge.EVENT_BUS.register(new PlayerInfo());		
		Minecraft.getMinecraft().gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, true);
		
		for(RenderPlayer render : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) { //Get Skin Types
			render.addLayer(new LayerCape(render)); //Add Cape to ALL skins
			render.addLayer(new Deadmau5(render)); //Assign ears to ALL skins			
			render.addLayer(new LayerElytra(render)); //Assign elyra
		}	
		
		for(Render render : Minecraft.getMinecraft().getRenderManager().entityRenderMap.values()) {
			if(render instanceof RenderBiped) {
				RenderLiving rl = (RenderLiving) render;
				rl.addLayer(new LayerElytra(rl));
			}
		}
	
		DeleteElytra.delete();
	}
}