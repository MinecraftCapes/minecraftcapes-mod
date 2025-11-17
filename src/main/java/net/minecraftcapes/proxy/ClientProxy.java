package net.minecraftcapes.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.events.KeyHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
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
    }
}
