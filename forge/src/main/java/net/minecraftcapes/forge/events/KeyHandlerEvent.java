package net.minecraftcapes.forge.events;

import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.gui.MenuScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MinecraftCapes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KeyHandlerEvent {

    @SubscribeEvent
    public static void onKeyPress(TickEvent.ClientTickEvent.Post event) {
        if (MinecraftCapes.KEY_MAPPING.consumeClick()) {
            Minecraft.getInstance().setScreen(new MenuScreen());
        }
    }
}
