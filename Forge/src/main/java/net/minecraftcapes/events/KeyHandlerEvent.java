package net.minecraftcapes.events;

import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapesConstants;
import net.minecraftcapes.MinecraftCapesForge;
import net.minecraftcapes.gui.MenuScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MinecraftCapesConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KeyHandlerEvent {

    @SubscribeEvent
    public static void onKeyPress(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            if (MinecraftCapesForge.menuKey.isDown()) {
                Minecraft.getInstance().setScreen(new MenuScreen());
            }
        }
    }

}
