package net.minecraftcapes.forge.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.gui.MenuScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
