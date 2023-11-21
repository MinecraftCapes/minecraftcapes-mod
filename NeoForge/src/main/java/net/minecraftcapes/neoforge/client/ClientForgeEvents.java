package net.minecraftcapes.neoforge.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.gui.MenuScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.event.TickEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = MinecraftCapes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
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
    public static void onClickTick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            if (KEY_MAPPING.get().consumeClick()) {
                Minecraft.getInstance().setScreen(new MenuScreen());
            }
        }
    }
}
