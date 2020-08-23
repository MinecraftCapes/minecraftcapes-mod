package net.minecraftcapes.proxy;

import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.events.FMLPostInitEvent;
import net.minecraftcapes.events.LabyModPostInitEvent;

public class ClientProxy implements IProxy {

    @Override
    public void postInit() {
        try {
            Class.forName("net.labymod.core.LabyModCore");
            MinecraftCapes.getLogger().info("Starting in LabyMod Mode");
            MinecraftCapes.setLabyMod(true);
            LabyModPostInitEvent.init();
        } catch (ClassNotFoundException e) {
            MinecraftCapes.getLogger().info("Starting in Forge Mode");
            FMLPostInitEvent.init();
        }
    }
}
