package net.minecraftcapes.proxy;

import net.minecraftcapes.compatibility.CaelusHook;
import net.minecraftcapes.events.EnqueueIMCEvent;
import net.minecraftcapes.events.PlayerEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy implements IProxy {

    @Override
    public void init() {
        //Do Mod compatibility checks :(
        if(doesClassExist("top.theillusivec4.caelus.api.CaelusApi")) {
            new CaelusHook();
        }

        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
    }

    @Override
    public void enqueueIMC(){
        EnqueueIMCEvent.init();
    }

    /**
     * Checks if a class exists or not
     * @param name
     * @return
     */
    private boolean doesClassExist(String name) {
        try {
            Class c = Class.forName(name);
            System.out.println(c);
            if (c != null) {
                return true;
            }
        } catch (ClassNotFoundException e) {}
        return false;
    }
}
