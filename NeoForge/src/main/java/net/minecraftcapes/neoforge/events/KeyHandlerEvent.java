package net.minecraftcapes.neoforge.events;

import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.gui.MenuScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;

@Mod.EventBusSubscriber(modid = MinecraftCapes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KeyHandlerEvent {

    @SubscribeEvent
    public static void onKeyPress(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            if (MinecraftCapes.KEY_MAPPING.consumeClick()) {
                Minecraft.getInstance().setScreen(new MenuScreen());
            }
        }
    }

}
