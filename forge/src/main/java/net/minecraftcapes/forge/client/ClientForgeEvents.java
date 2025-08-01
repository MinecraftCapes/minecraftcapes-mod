package net.minecraftcapes.forge.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.gui.MenuScreen;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class ClientForgeEvents {
    
    public static final Lazy<KeyMapping> KEY_MAPPING = Lazy.of(() -> new KeyMapping(
            "key." + MinecraftCapes.MOD_ID + ".gui",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            "category." + MinecraftCapes.MOD_ID + ".gui"
    ));
    
    /**
     * Register the client tick for key listening
     */
    @SubscribeEvent
    public static void onClickTick(TickEvent.ClientTickEvent.Pre event) {
        if (KEY_MAPPING.get().consumeClick()) {
            Minecraft.getInstance().setScreen(new MenuScreen());
        }
    }
}
