package net.minecraftcapes.proxy;

import net.minecraftcapes.events.FMLPostInitEvent;

public class ClientProxy implements IProxy {

    @Override
    public void postInit(){
        FMLPostInitEvent.init();
    }
}
