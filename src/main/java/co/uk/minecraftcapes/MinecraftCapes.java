package co.uk.minecraftcapes;

import co.uk.minecraftcapes.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.MC_VERSION)
public class MinecraftCapes {
	
	//Define client and server proxy
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	public static String MOD_DIR;
	
	//PreInitialisation
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();
		MOD_DIR = event.getModConfigurationDirectory().getAbsolutePath();
	}
	
	//Initialisation
	@EventHandler
	public void init(FMLInitializationEvent event) {	
	}
	
	//PostInitialisation
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {		
	}	
	
}
