package net.minecraftcapes;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraftcapes.proxy.IProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = MinecraftCapes.MODID, name = "James090500's MinecraftCapes Mod", version = "10", acceptedMinecraftVersions = "1.7.10")
public class MinecraftCapes {

	public static final String MODID = "minecraftcapes";

	@Getter
	private static final Logger logger = LogManager.getLogger();
	@SidedProxy(clientSide = "net.minecraftcapes.proxy.ClientProxy", serverSide = "net.minecraftcapes.proxy.ServerProxy")
	private static IProxy proxy;

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}
}
