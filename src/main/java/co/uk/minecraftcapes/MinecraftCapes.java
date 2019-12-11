package co.uk.minecraftcapes;

import static co.uk.minecraftcapes.reference.Reference.MODID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;

public class MinecraftCapes implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger(MODID);
	
	@Override
	public void onInitialize() {
		LOGGER.info("[MinecraftCapes] Initialised");
	}
}
