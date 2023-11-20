package net.minecraftcapes.forge.events;

import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.forge.ForgeImplementation;
import net.minecraftcapes.gui.MenuScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MinecraftCapes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KeyHandlerEvent {
    
    /**
     * Register the keybinds
     * @param event
     */
    @SubscribeEvent
    public void registerKeyBinding(RegisterKeyMappingsEvent event) {
        event.register(ForgeImplementation.KEY_MAPPING);
    }
    
    @SubscribeEvent
    public void onClickTick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            if (ForgeImplementation.KEY_MAPPING.consumeClick()) {
                Minecraft.getInstance().setScreen(new MenuScreen());
            }
        }
    }

}
