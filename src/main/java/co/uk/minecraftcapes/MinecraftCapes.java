package co.uk.minecraftcapes;

import static co.uk.minecraftcapes.reference.Reference.MODID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import co.uk.minecraftcapes.proxy.ClientProxy;
import co.uk.minecraftcapes.proxy.IProxy;
import co.uk.minecraftcapes.proxy.ServerProxy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MODID)
public class MinecraftCapes {

	private static final Logger LOGGER = LogManager.getLogger();


	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

	public MinecraftCapes() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(MinecraftCapes::enqueueIMC);

        proxy.init();
	}
	
	//PostInitialisation
	@SubscribeEvent
	public static void enqueueIMC(InterModEnqueueEvent event) {
		proxy.enqueueIMC();


	}

	public static Logger getLogger(){
		return LOGGER;
	}
	
}
