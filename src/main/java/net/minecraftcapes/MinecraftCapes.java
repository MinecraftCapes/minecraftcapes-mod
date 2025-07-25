package net.minecraftcapes;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraftcapes.proxy.IProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = MinecraftCapes.MODID, name = "MinecraftCapes Mod", version = "11.2.0", acceptedMinecraftVersions = "1.7.10")
public class MinecraftCapes {

	public static final String MODID = "minecraftcapes";
	public static final String MINECRAFT_VERSION = "1.7.10";

	@Getter private static final Logger logger = LogManager.getLogger();

	@Getter @Setter private static boolean isLabyMod = false;

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
