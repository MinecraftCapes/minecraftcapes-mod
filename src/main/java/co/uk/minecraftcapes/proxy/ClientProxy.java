package co.uk.minecraftcapes.proxy;

import co.uk.minecraftcapes.events.EnqueueIMCEvent;
import co.uk.minecraftcapes.events.PlayerEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy implements IProxy{

    @Override
    public void init() {


        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
    }

    @Override
    public void enqueueIMC(){
        EnqueueIMCEvent.init();
    }
}
