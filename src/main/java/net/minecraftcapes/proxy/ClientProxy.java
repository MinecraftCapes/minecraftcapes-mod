package net.minecraftcapes.proxy;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.events.MinecraftCapesLabyModPostInit;
import net.minecraftcapes.events.MinecraftCapesPostInit;
import net.minecraftcapes.events.PlayerEventHandler;
import net.minecraftcapes.gui.GuiHandler;
import net.minecraftcapes.gui.MenuScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ClientProxy implements IProxy {

    public static final KeyBinding menuKey = new KeyBinding("key.minecraftcapes.gui", 74, "category.minecraftcapes.gui");

    @Override
    public void init() {
        //Register the menu
//        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        //Register the keybinds
        ClientRegistry.registerKeyBinding(menuKey);
    }

    @Override
    public void postInit() {
        MinecraftCapesConfig.loadConfig();
        MinecraftForge.EVENT_BUS.register(new MenuScreen());
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        try {
            Class.forName("net.labymod.core.LabyModCore");
            MinecraftCapes.getLogger().debug("Starting in LabyMod Mode");
            MinecraftCapes.setLabyMod(true);
            MinecraftCapesLabyModPostInit.init();
        } catch (ClassNotFoundException e) {
            MinecraftCapes.getLogger().debug("Starting in Forge Mode");
            MinecraftCapesPostInit.init();
        }
    }
}
