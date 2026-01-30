package net.minecraftcapes;

import lombok.Getter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.options.KeyBinding;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.gui.MenuScreen;
import net.ornithemc.osl.lifecycle.api.MinecraftEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MinecraftCapes implements ClientModInitializer {

    public static final String MOD_ID = "minecraftcapes";
    public static final String MINECRAFT_VERSION = "b1.7.3";

    @Getter private static final Logger logger = LogManager.getLogger("MinecraftCapes");
    @Getter private static final Path configDir = FabricLoaderImpl.INSTANCE.getConfigDir().resolve(MOD_ID);

    private static final List<Runnable> scheduledTasks = new ArrayList<>();

    public static final KeyBinding keyBinding = new KeyBinding("Open MCC GUI", Keyboard.KEY_J);

    @Override
    public void onInitializeClient() {
        getLogger().info("Initialising");

        //Loading Config
        MinecraftCapesConfig.loadConfig();

        getLogger().info("Initialised");

        // Scheduler
        MinecraftEvents.TICK_END.register(minecraft -> {
            if(!scheduledTasks.isEmpty()) {
                Runnable runnable = scheduledTasks.remove(0);
                runnable.run();
            }

            if(Keyboard.isKeyDown(keyBinding.keyCode)) {
                if(minecraft.screen == null) {
                    minecraft.openScreen(new MenuScreen());
                }
            }
        });
    }

    public static void runLater(Runnable runnable) {
        scheduledTasks.add(runnable);
    }
}
