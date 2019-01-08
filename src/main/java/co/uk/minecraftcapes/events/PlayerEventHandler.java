package co.uk.minecraftcapes.events;

import java.util.HashMap;

import co.uk.minecraftcapes.helpers.Loader;
import co.uk.minecraftcapes.player.downloader.DeleteElytra;
import co.uk.minecraftcapes.player.downloader.DownloadCape;
import co.uk.minecraftcapes.player.downloader.DownloadEars;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static co.uk.minecraftcapes.reference.Reference.MODID;

public class PlayerEventHandler {
	
	private static boolean resetElytra = false;
	public static HashMap<String, Boolean> playersCape = new HashMap<String, Boolean>();
	public static HashMap<String, Boolean> playersEar = new HashMap<String, Boolean>();
	
	@SubscribeEvent
	public void onPlayerJoin(EntityJoinWorldEvent event) {
		if(event.getWorld().isRemote) {
			if(event.getEntity() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.getEntity();				
				String uuid = player.getUniqueID().toString().replace("-", "");
				Loader.download(uuid, "Cape", 64, 32);
				Loader.download(uuid, "Ears", 64, 64);
		    	if(!resetElytra) {
		    		DeleteElytra.delete();
		    		resetElytra = true;
		    	}
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
	    ResourceLocation resourceLocation = new ResourceLocation(MODID, "cape/" + playerUUID);
	    return hasCape(playerUUID) ? resourceLocation : null;
	}
	
	public static ResourceLocation getEarResourceLocation(EntityLivingBase entitylivingbaseIn) {
	    String playerUUID = entitylivingbaseIn.getUniqueID().toString().replace("-", "");
	    ResourceLocation resourceLocation = new ResourceLocation(MODID, "ears/" + playerUUID);
	    return hasEar(playerUUID) ? resourceLocation : null;
	}

}
