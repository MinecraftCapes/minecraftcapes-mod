package net.minecraftcapes;

import lombok.Getter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.option.KeyBinding;
import net.minecraftcapes.config.MinecraftCapesConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MinecraftCapes implements ClientModInitializer {

    public static final String MOD_ID = "minecraftcapes";
    public static final String MINECRAFT_VERSION = "b1.7.3";

    @Getter private static final Logger logger = LogManager.getLogger("MinecraftCapes");
    @Getter private static final Path configDir = FabricLoaderImpl.INSTANCE.getConfigDir().resolve(MOD_ID);

    @Getter private static final List<Runnable> scheduledTasks = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        getLogger().info("Initialising");

        //Loading Config
        MinecraftCapesConfig.loadConfig();

        getLogger().info("Initialised");
    }
}
