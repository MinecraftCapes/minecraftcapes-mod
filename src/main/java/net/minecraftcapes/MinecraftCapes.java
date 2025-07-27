package net.minecraftcapes;

import lombok.Getter;
import net.minecraftcapes.proxy.ClientProxy;
import net.minecraftcapes.proxy.IProxy;
import net.minecraftcapes.proxy.ServerProxy;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MinecraftCapes.MODID)
public class MinecraftCapes {

	public static final String MODID = "minecraftcapes";
	public static final String MINECRAFT_VERSION = "1.13.2";

	@Getter private static final Logger logger = LogManager.getLogger();
	public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

	public MinecraftCapes() {
		getLogger().info("[MinecraftCapes] Initialising");

		FMLJavaModLoadingContext.get().getModEventBus().addListener(proxy::clientSetup);

		proxy.init();

		getLogger().info("[MinecraftCapes] Initialised");
	}
}
