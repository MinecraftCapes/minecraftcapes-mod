package net.minecraftcapes;

import lombok.Getter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.gui.MenuScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.nio.file.Path;

public class MinecraftCapes implements ClientModInitializer {

    public static final String MOD_ID = "minecraftcapes";
    public static final String MINECRAFT_VERSION = Minecraft.getInstance().getVersionType();

    @Getter private static final Logger logger = LogManager.getLogger();
    @Getter private static final Path configDir = FabricLoaderImpl.INSTANCE.getConfigDir().resolve(MOD_ID);

    private static KeyMapping keyBinding;
    public static final KeyMapping keyMapping = new KeyMapping(
            "key.minecraftcapes.gui",
            GLFW.GLFW_KEY_J,
            "category.minecraftcapes.gui"
    );

    @Override
    public void onInitializeClient() {
        getLogger().info("[MinecraftCapes] Initialising");

        //Loading Config
        MinecraftCapesConfig.loadConfig();

        //Configure the KeyBind
        keyBinding = KeyBindingHelper.registerKeyBinding(keyMapping);

        //React to key pressed
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(keyBinding.isDown()) {
                Minecraft.getInstance().setScreen(new MenuScreen());
            }
        });

        getLogger().info("[MinecraftCapes] Initialised");
    }
}
