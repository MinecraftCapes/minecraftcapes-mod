package net.minecraftcapes.proxy;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.events.KeyHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ClientProxy implements IProxy {

    public static final KeyBinding menuKey = new KeyBinding("key.minecraftcapes.gui", 74, "category.minecraftcapes.gui");

    @Override
    public void init() {
        //Prep the config
        MinecraftCapesConfig.loadConfig();

        MinecraftForge.EVENT_BUS.register(new KeyHandlerEvent());
    }

    @Override
    public void clientSetup(FMLCommonSetupEvent event) {
        //Register the keybinds
        ClientRegistry.registerKeyBinding(menuKey);
    }
}
