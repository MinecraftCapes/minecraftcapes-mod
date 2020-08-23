package net.minecraftcapes;

import lombok.Getter;
import lombok.Setter;
import net.minecraftcapes.proxy.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = MinecraftCapes.MODID, name = "James090500's MinecraftCapes Mod", version = "10", acceptedMinecraftVersions = "1.8.9")
public class MinecraftCapes {

	public static final String MODID = "minecraftcapes";
	@Getter @Setter private static boolean isLabyMod = false;

	@Getter
	private static final Logger logger = LogManager.getLogger();
	@SidedProxy(clientSide = "net.minecraftcapes.proxy.ClientProxy", serverSide = "net.minecraftcapes.proxy.ServerProxy")
	private static IProxy proxy;

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}
}
