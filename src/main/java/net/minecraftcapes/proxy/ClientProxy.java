package net.minecraftcapes.proxy;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.events.KeyHandlerEvent;
import net.minecraftcapes.events.MinecraftCapesLabyModPostInit;
import net.minecraftcapes.events.MinecraftCapesPostInit;
import net.minecraftcapes.events.PlayerEventHandler;
import net.minecraftcapes.gui.MenuScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.input.Keyboard;

public class ClientProxy implements IProxy {

    public static final KeyBinding menuKey = new KeyBinding("key.minecraftcapes.gui", Keyboard.KEY_J, "category.minecraftcapes.gui");

    @Override
    public void init() {
        //Prep the config
        MinecraftCapesConfig.loadConfig();

        //Register the keybinds
        ClientRegistry.registerKeyBinding(menuKey);
    }

    @Override
    public void postInit() {
        MinecraftForge.EVENT_BUS.register(new KeyHandlerEvent());
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
