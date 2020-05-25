package net.minecraftcapes;

import net.minecraftcapes.proxy.ClientProxy;
import net.minecraftcapes.proxy.IProxy;
import net.minecraftcapes.proxy.ServerProxy;
import lombok.Getter;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MinecraftCapes.MODID)
public class MinecraftCapes {

	public static final String MODID = "minecraftcapes";

	@Getter
	private static final Logger logger = LogManager.getLogger();
	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

	public MinecraftCapes() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(MinecraftCapes::enqueueIMC);
        proxy.init();
	}

	@SubscribeEvent
	public static void enqueueIMC(InterModEnqueueEvent event) {
		proxy.enqueueIMC();
	}
}
