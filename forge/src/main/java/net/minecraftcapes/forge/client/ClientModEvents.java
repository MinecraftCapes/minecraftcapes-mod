package net.minecraftcapes.forge.client;

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientModEvents {
    
    /**
     * Register the keybinds
     */
    @SubscribeEvent
    public static void registerKeyBinding(RegisterKeyMappingsEvent event) {
        event.register(ClientForgeEvents.KEY_MAPPING.get());
    }
}
