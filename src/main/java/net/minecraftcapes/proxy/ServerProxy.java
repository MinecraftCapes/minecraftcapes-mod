package net.minecraftcapes.proxy;

import net.minecraftcapes.MinecraftCapes;

public class ServerProxy implements IProxy{

    @Override
    public void init() {
        MinecraftCapes.getLogger().error("MinecraftCapes has been loaded on server side. MinecraftCapes is a client only mod. No need to worry about this. You can delete the mod if you wish!");
    }
}
