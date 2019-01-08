package co.uk.minecraftcapes.proxy;

import co.uk.minecraftcapes.player.PlayerInfo;
import co.uk.minecraftcapes.player.render.RenderCape;
import co.uk.minecraftcapes.player.render.RenderDeadmau5;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy implements CommonProxy {
	
	//Calling ONLY on client side
	@Override
	public void preInit() {
		
		MinecraftForge.EVENT_BUS.register(new PlayerInfo());
		MinecraftForge.EVENT_BUS.register(new RenderCape());
		MinecraftForge.EVENT_BUS.register(new RenderDeadmau5());
	}
}