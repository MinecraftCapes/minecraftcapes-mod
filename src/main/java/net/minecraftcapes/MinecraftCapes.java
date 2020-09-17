package net.minecraftcapes;

import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.minecraftcapes.compatibility.TrinketsHook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MinecraftCapes implements ModInitializer {

	public static final String MODID = "minecraftcapes";
	@Getter private static final Logger Logger = LogManager.getLogger(MODID);
	
	@Override
	public void onInitialize() {
		getLogger().info("[MinecraftCapes] Initialised");

		//Do Mod compatibility checks :(
		if(doesClassExist("dev.emi.trinkets.TrinketsMain")) {
			new TrinketsHook();
		}
	}

	/**
	 * Checks if a class exists or not
	 * @param name
	 * @return
	 */
	private boolean doesClassExist(String name) {
		try {
			Class c = Class.forName(name);
			System.out.println(c);
			if (c != null) {
				return true;
			}
		} catch (ClassNotFoundException e) {}
		return false;
	}
}
