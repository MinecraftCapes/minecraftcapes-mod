package net.minecraftcapes.proxy;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.events.KeyHandlerEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.input.Keyboard;

public class ClientProxy implements CommonProxy {

    public static final KeyBinding menuKey = new KeyBinding(
            "key.minecraftcapes.gui",
            Keyboard.KEY_J,
            "category.minecraftcapes.gui"
    );

    @Override
    public void init() {
        MinecraftCapes.getLogger().info("Initialising");

        //Prep the config
        MinecraftCapesConfig.loadConfig();

        //Register the keybinds
        ClientRegistry.registerKeyBinding(menuKey);

        MinecraftCapes.getLogger().info("Initialised");
    }

    @Override
    public void postInit() {
        FMLCommonHandler.instance().bus().register(new KeyHandlerEvent());
    }
}
