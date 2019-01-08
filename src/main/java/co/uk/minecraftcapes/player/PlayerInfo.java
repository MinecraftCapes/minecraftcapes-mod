package co.uk.minecraftcapes.player;

import java.lang.reflect.Field;
import java.util.HashMap;

import co.uk.minecraftcapes.UpdateManager;
import co.uk.minecraftcapes.player.downloader.DownloadCape;
import co.uk.minecraftcapes.player.downloader.DownloadEars;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import net.minecraft.client.renderer.entity.RenderPlayer;

public class PlayerInfo {
	
	public static HashMap<String, Boolean> playersCape = new HashMap<String, Boolean>();
	public static HashMap<String, Boolean> playersEar = new HashMap<String, Boolean>();
	
	@SubscribeEvent
	public void PlayerJoin(EntityJoinWorldEvent event) {		
		if(event.world.isRemote) {
			if(event.entity instanceof EntityPlayer) {
				UpdateManager.getUpdate();
				String uuid = event.entity.getUniqueID().toString().replace("-", "");
				DownloadCape.download(uuid);
				DownloadEars.download(uuid);
			}
		}
	}
	
	  private static Boolean hasCape(String uuid) {
	    Boolean hashMapResult = playersCape.get(uuid);	   
	    if (hashMapResult == null) return false;
	    return hashMapResult;
	  }
	
	  private static Boolean hasEar(String uuid) {
	    Boolean hashMapResult = playersEar.get(uuid);
	    if (hashMapResult == null) return false;
	    return hashMapResult;
	  }
	
	  public static ResourceLocation getCapeResourceLocation(EntityLivingBase entitylivingbaseIn) {
	    String playerUUID = entitylivingbaseIn.getUniqueID().toString().replace("-", "");
	    ResourceLocation resourceLocation = new ResourceLocation("capes/" + playerUUID);
	    return hasCape(playerUUID) ? resourceLocation : null;
	  }

	  public static ResourceLocation getEarResourceLocation(EntityLivingBase entitylivingbaseIn) {
	    String playerUUID = entitylivingbaseIn.getUniqueID().toString().replace("-", "");
	    ResourceLocation resourceLocation = new ResourceLocation("ears/" + playerUUID);
	    return hasEar(playerUUID) ? resourceLocation : null;
	  }
		  
}
