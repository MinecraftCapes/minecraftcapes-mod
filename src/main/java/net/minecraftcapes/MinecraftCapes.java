package net.minecraftcapes;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import lombok.Getter;
import net.minecraftcapes.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

@Mod(modid = MinecraftCapes.MOD_ID, name = MinecraftCapes.MOD_NAME, version = "@MOD_VERSION", acceptedMinecraftVersions = "@MINECRAFT_VERSION")
public class MinecraftCapes {

	public static final String MOD_ID = "minecraftcapes";
    public static final String MOD_NAME = "MinecraftCapes";
	public static final String MINECRAFT_VERSION = MinecraftForge.MC_VERSION;

	@Getter private static final Logger logger = LogManager.getLogger(MOD_NAME);
    @Getter private static final Path configDir = Loader.instance().getConfigDir().toPath().resolve(MOD_ID);

	@SidedProxy(clientSide = "net.minecraftcapes.proxy.ClientProxy", serverSide = "net.minecraftcapes.proxy.ServerProxy")
	private static CommonProxy commonProxy;

	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
        commonProxy.init();
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
        commonProxy.postInit();
	}
}
