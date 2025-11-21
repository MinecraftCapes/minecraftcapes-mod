package net.minecraftcapes;

import lombok.Getter;
import net.minecraftcapes.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

@Mod(modid = MinecraftCapes.MOD_ID, name = MinecraftCapes.MOD_NAME, version = "1.0.0")
public class MinecraftCapes {

    public static final String MOD_ID = "minecraftcapes";
    public static final String MOD_NAME = "MinecraftCapes";
    public static final String MINECRAFT_VERSION = MinecraftForge.MC_VERSION;

    @Getter private static final Logger logger = LogManager.getLogger(MOD_NAME);
    @Getter private static final Path configDir = Loader.instance().getConfigDir().toPath().resolve(MOD_ID);

	@SidedProxy(clientSide = "net.minecraftcapes.proxy.ClientProxy", serverSide = "net.minecraftcapes.proxy.ServerProxy")
	private static CommonProxy commonProxy;

	@Mod.EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
        commonProxy.init();
	}

	@Mod.EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
        commonProxy.postInit();
	}
}
