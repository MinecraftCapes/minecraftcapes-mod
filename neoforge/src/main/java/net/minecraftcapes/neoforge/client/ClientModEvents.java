package net.minecraftcapes.neoforge.client;

import net.minecraftcapes.MinecraftCapes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(modid = MinecraftCapes.MOD_ID, value = Dist.CLIENT)
public class ClientModEvents {
    
    /**
     * Register the keybinds
     */
    @SubscribeEvent
    public static void registerKeyBinding(RegisterKeyMappingsEvent event) {
        event.register(MinecraftCapes.KEY_MAPPING.get());
    }
}
