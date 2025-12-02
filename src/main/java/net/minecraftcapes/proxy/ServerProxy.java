package net.minecraftcapes.proxy;

import net.minecraftcapes.MinecraftCapes;

public class ServerProxy implements CommonProxy {

    @Override
    public void init() {}

    @Override
    public void postInit() {
        MinecraftCapes.getLogger().error("=============================================");
        MinecraftCapes.getLogger().error("MinecraftCapes only needs to be on the client");
        MinecraftCapes.getLogger().error("     You'll still see each others capes!");
        MinecraftCapes.getLogger().error("     Please remove this from your server!");
        MinecraftCapes.getLogger().error("=============================================");
    }
}
