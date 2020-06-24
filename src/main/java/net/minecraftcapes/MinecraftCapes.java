package net.minecraftcapes;

import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MinecraftCapes implements ModInitializer {

	public static final String MODID = "minecraftcapes";
	@Getter private static final Logger Logger = LogManager.getLogger(MODID);
	
	@Override
	public void onInitialize() {
		getLogger().info("[MinecraftCapes] Initialised");
	}
}
