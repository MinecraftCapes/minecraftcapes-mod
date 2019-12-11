package co.uk.minecraftcapes.events;

import java.util.HashMap;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;

import static co.uk.minecraftcapes.reference.Reference.MODID;

public class PlayerEventHandler {
		
	public static HashMap<String, Boolean> playersCape = new HashMap<String, Boolean>();
	public static HashMap<String, Boolean> playersEar = new HashMap<String, Boolean>();

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
	
	public static Identifier getCapeResourceLocation(AbstractClientPlayerEntity acpe) {
	    String playerUUID = acpe.getUuid().toString().replace("-", "");
	    Identifier resourceLocation = new Identifier(MODID, "capes/" + playerUUID);
	    return hasCape(playerUUID) ? resourceLocation : null;
	}
	
	public static Identifier getEarResourceLocation(AbstractClientPlayerEntity acpe) {
	    String playerUUID = acpe.getUuid().toString().replace("-", "");
	    Identifier resourceLocation = new Identifier(MODID, "ears/" + playerUUID);
	    return hasEar(playerUUID) ? resourceLocation : null;
	}

}
