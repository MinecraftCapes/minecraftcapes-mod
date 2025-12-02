package net.minecraftcapes.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraftcapes.gui.MenuScreen;
import net.minecraftcapes.proxy.ClientProxy;

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
