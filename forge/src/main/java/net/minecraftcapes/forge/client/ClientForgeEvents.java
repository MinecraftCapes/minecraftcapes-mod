package net.minecraftcapes.forge.client;

import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.gui.MenuScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MinecraftCapes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {
    
    /**
     * Register the client tick for key listening
     */
    @SubscribeEvent
    public static void onClickTick(TickEvent.ClientTickEvent.Post event) {
        if (MinecraftCapes.KEY_MAPPING.get().consumeClick()) {
            Minecraft.getInstance().setScreen(new MenuScreen());
        }
    }
}
