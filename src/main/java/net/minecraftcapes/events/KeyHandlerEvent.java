package net.minecraftcapes.events;

import net.minecraft.client.Minecraft;
import net.minecraftcapes.gui.MenuScreen;
import net.minecraftcapes.proxy.ClientProxy;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class KeyHandlerEvent {

    @SubscribeEvent
    public void onKeyPress(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            if (ClientProxy.menuKey.isPressed()) {
                Minecraft.getMinecraft().displayGuiScreen(new MenuScreen());
            }
        }
    }

}
