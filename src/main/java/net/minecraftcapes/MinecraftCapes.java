package net.minecraftcapes;

import lombok.Getter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.KeyBinding;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.gui.MenuScreen;
import net.modificationstation.stationapi.api.client.event.keyboard.KeyStateChangedEvent;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.nio.file.Path;

public class MinecraftCapes implements ClientModInitializer {

    public static final String MOD_ID = "minecraftcapes";
    public static final String MINECRAFT_VERSION = "b1.7.3";

    @Getter private static final Logger logger = LogManager.getLogger();
    @Getter private static final Path configDir = FabricLoaderImpl.INSTANCE.getConfigDir().resolve(MOD_ID);

    private static KeyBinding keyBinding;

    @Override
    public void onInitializeClient() {
        getLogger().info("[MinecraftCapes] Initialising");

        //Loading Config
        MinecraftCapesConfig.loadConfig();

        getLogger().info("[MinecraftCapes] Initialised");
    }

    @EventListener
    public void registerKeybinds(KeyBindingRegisterEvent event) {
        keyBinding = new KeyBinding("Open GUI", Keyboard.KEY_J);
        event.keyBindings.add(keyBinding);
    }

    @EventListener
    public void handle(KeyStateChangedEvent event) {
        if (Keyboard.getEventKeyState()) {
            if (Keyboard.isKeyDown(keyBinding.code)) {
                if (event.environment == KeyStateChangedEvent.Environment.IN_GAME) {
                    Minecraft gameInstance = (Minecraft) FabricLoader.getInstance().getGameInstance();
                    gameInstance.setScreen(new MenuScreen());
                }
            }
        }
    }
}
