package co.uk.minecraftcapes;

import co.uk.minecraftcapes.proxy.ClientProxy;
import co.uk.minecraftcapes.proxy.IProxy;
import co.uk.minecraftcapes.proxy.ServerProxy;
import co.uk.minecraftcapes.reference.Reference;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static co.uk.minecraftcapes.reference.Reference.MODID;

@Mod(Reference.MODID)
public class MinecraftCapes {

	@Getter
	private static final Logger logger = LogManager.getLogger();
	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

	public MinecraftCapes() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(MinecraftCapes::onCommonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(MinecraftCapes::enqueueIMC);
        proxy.init();
	}

	@SubscribeEvent
	public static void onCommonSetup(FMLCommonSetupEvent event) { proxy.setup(); }

	@SubscribeEvent
	public static void enqueueIMC(InterModEnqueueEvent event) {
		proxy.enqueueIMC();
	}
}
