package net.minecraftcapes.events;

import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.gui.MenuScreen;
import net.minecraftcapes.proxy.ClientProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = MinecraftCapes.MODID, value = Side.CLIENT)
public class KeyHandlerEvent {

    @SubscribeEvent
    public static void onKeyPress(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            if (ClientProxy.menuKey.isKeyDown()) {
                Minecraft.getMinecraft().displayGuiScreen(new MenuScreen());
            }
        }
    }

}
