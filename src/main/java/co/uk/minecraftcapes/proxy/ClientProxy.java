package co.uk.minecraftcapes.proxy;

import co.uk.minecraftcapes.MinecraftCapesForgeBus;
import co.uk.minecraftcapes.capabilities.PlayerHandler;
import co.uk.minecraftcapes.events.EnqueueIMCEvent;
import co.uk.minecraftcapes.events.PlayerEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ClientProxy implements IProxy {

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(MinecraftCapesForgeBus.class);
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
    }

    @Override
    public void setup() {
        CapabilityManager.INSTANCE.register(PlayerHandler.class, new PlayerHandler.Storage(), PlayerHandler::new);
    }

    @Override
    public void enqueueIMC(){
        EnqueueIMCEvent.init();
    }
}
