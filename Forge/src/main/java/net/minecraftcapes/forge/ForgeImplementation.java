package net.minecraftcapes.forge;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.forge.events.KeyHandlerEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MinecraftCapes.MOD_ID)
public class ForgeImplementation extends MinecraftCapes {
    public static KeyMapping keyMapping;
    public ForgeImplementation() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }
    
    /**
     * This client setup event
     * @param event
     */
    public void clientSetup(FMLClientSetupEvent event) {
        MinecraftCapes.onEnable();
        
        //Register the events
        MinecraftForge.EVENT_BUS.register(new KeyHandlerEvent());
        
        //Try turn on capes
        Minecraft.getInstance().options.toggleModelPart(PlayerModelPart.CAPE, true);
    
        //Register the key
        ForgeImplementation.keyMapping = new KeyMapping("key.minecraftcapes.gui", 74, "category.minecraftcapes.gui");
        
        MinecraftCapes.getLogger().info("Initialised");
    }
    
    /**
     * Register the keybinds
     * @param event
     */
    @SubscribeEvent
    public void registerKeyBinding(RegisterKeyMappingsEvent event) {
        event.register(keyMapping);
    }
    
}
