package net.minecraftcapes.neoforge.client;

import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.gui.MenuScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = MinecraftCapes.MOD_ID, value = Dist.CLIENT)
public class ClientForgeEvents {
    
    /**
     * Register the client tick for key listening
     */
    @SubscribeEvent
    public static void onClickTick(ClientTickEvent.Post event) {
        if (MinecraftCapes.KEY_MAPPING.get().consumeClick()) {
            Minecraft.getInstance().setScreen(new MenuScreen());
        }
    }
}
