package net.minecraftcapes.events;

import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.gui.MenuScreen;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyHandlerEvent {

    @SubscribeEvent
    public void onKeyPress(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            if (MinecraftCapes.menuKey.isDown()) {
                Minecraft.getInstance().setScreen(new MenuScreen());
            }
        }
    }

}
