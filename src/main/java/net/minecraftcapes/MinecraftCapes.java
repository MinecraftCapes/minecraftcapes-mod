package net.minecraftcapes;

import lombok.Getter;
import net.minecraftcapes.proxy.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = MinecraftCapes.MODID, name = "MinecraftCapes Mod", version = "11.2.0", acceptedMinecraftVersions = "1.12.2")
public class MinecraftCapes {

	public static final String MODID = "minecraftcapes";
	public static final String MINECRAFT_VERSION = "1.12.2";

	@Getter private static final Logger logger = LogManager.getLogger();

	@SidedProxy(clientSide = "net.minecraftcapes.proxy.ClientProxy", serverSide = "net.minecraftcapes.proxy.ServerProxy")
	private static IProxy proxy;

	@Mod.EventHandler
	public static void preInit(FMLPostInitializationEvent event) {
		proxy.init();
	}

	@Mod.EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}
}
