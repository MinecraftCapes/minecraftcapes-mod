package net.minecraftcapes.neoforge.events;

import net.minecraftcapes.MinecraftCapes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(modid = MinecraftCapes.MOD_ID, value = Dist.CLIENT)
public class RegisterKeyEvent {
    
    /**
     * Register the keybinds
     * @param event
     */
    @SubscribeEvent
    public static void registerKeyBinding(RegisterKeyMappingsEvent event) {
        event.register(MinecraftCapes.KEY_MAPPING);
    }
}
