package net.minecraftcapes.neoforge.events;

import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.gui.MenuScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = MinecraftCapes.MOD_ID, value = Dist.CLIENT)
public class KeyHandlerEvent {

    @SubscribeEvent
    public static void onKeyPress(ClientTickEvent.Post event) {
        if (MinecraftCapes.KEY_MAPPING.consumeClick()) {
            Minecraft.getInstance().gui.setScreen(new MenuScreen());
        }
    }

}
