package net.minecraftcapes.forge.client;

import net.minecraftcapes.MinecraftCapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MinecraftCapes.MOD_ID, value = Dist.CLIENT)
public class ClientModEvents {
    
    /**
     * Register the keybinds
     */
    @SubscribeEvent
    public static void registerKeyBinding(RegisterKeyMappingsEvent event) {
        event.register(MinecraftCapes.KEY_MAPPING.get());
    }
}
