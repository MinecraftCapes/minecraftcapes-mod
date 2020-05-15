package co.uk.minecraftcapes.events;

import static co.uk.minecraftcapes.reference.Reference.MODID;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import co.uk.minecraftcapes.helpers.AnimatedCapeHandler;
import co.uk.minecraftcapes.player.downloader.DownloadCape;
import co.uk.minecraftcapes.player.downloader.DownloadEars;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerEventHandler {

	private static HashMap<UUID, Boolean> playersCape = new HashMap<UUID, Boolean>();
	private static HashMap<UUID, Boolean> playersEars = new HashMap<UUID, Boolean>();
	private static HashMap<UUID, Int2ObjectMap<NativeImage>> playersAnimatedCape = new HashMap<UUID, Int2ObjectMap<NativeImage>>();

	@SubscribeEvent
	public void onPlayerJoin(EntityJoinWorldEvent event) {
		if(event.getEntity() instanceof PlayerEntity) {
			UUID playerUUID = event.getEntity().getUniqueID();
			if(playersCape.get(playerUUID) == null && !playersAnimatedCape.containsKey(playerUUID)) {
				DownloadCape.download(event.getEntity().getUniqueID());
			}

			if(playersEars.get(playerUUID) == null) {
				DownloadEars.download(event.getEntity().getUniqueID());
			}
		}
	}
	
	private static Boolean hasCape(UUID uuid) {
		Boolean hashmapResult = playersCape.get(uuid);
		return (playersCape.get(uuid) == null) ? false : hashmapResult;
	}
	
	private static Boolean hasEars(UUID uuid) {
		Boolean hashmapResult = playersEars.get(uuid);
		return (playersEars.get(uuid) == null) ? false : hashmapResult;
	}

	public static Boolean hasAnimatedCape(UUID uuid) {
		return playersAnimatedCape.containsKey(uuid);
	}

	public static void setCape(UUID uuid) {
		playersCape.put(uuid, true);
	}

	public static void setEars(UUID uuid) {
		playersEars.put(uuid, true);
	}

	public static void setAnimatedCape(UUID playerUUID, Int2ObjectMap<NativeImage> animatedCape) {
		playersAnimatedCape.put(playerUUID, animatedCape);
	}

	public static Int2ObjectMap<NativeImage> getAnimatedCape(UUID playerUUID) {
		return playersAnimatedCape.get(playerUUID);
	}

	public static ResourceLocation getCapeResourceLocation(AbstractClientPlayerEntity abstractClientPlayerEntity) {
		UUID playerUUID = abstractClientPlayerEntity.getUniqueID();
	    if(hasCape(playerUUID)) {
	    	return new ResourceLocation(MODID, "capes/" + playerUUID);
		} else if(hasAnimatedCape(playerUUID)) {
	    	return AnimatedCapeHandler.getPlayer(playerUUID).getFrame();
		} else {
	    	return null;
		}
	}
	
	public static ResourceLocation getEarResourceLocation(AbstractClientPlayerEntity abstractClientPlayerEntity) {
	    ResourceLocation resourceLocation = new ResourceLocation(MODID, "ears/" + abstractClientPlayerEntity.getUniqueID());
	    return hasEars(abstractClientPlayerEntity.getUniqueID()) ? resourceLocation : null;
	}

}
