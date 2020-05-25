package net.minecraftcapes.proxy;

import net.minecraftcapes.events.EnqueueIMCEvent;
import net.minecraftcapes.events.PlayerEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy implements IProxy {

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
    }

    @Override
    public void enqueueIMC(){
        EnqueueIMCEvent.init();
    }
}
