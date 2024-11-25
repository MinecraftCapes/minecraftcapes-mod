package net.minecraftcapes.forge;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.forge.client.ClientForgeEvents;
import net.minecraftcapes.forge.client.ClientModEvents;
import net.minecraftcapes.forge.server.ServerModEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MinecraftCapes.MOD_ID)
public class ForgeImplementation {
    
    public ForgeImplementation(FMLJavaModLoadingContext context) {
        context.getModEventBus().addListener(this::clientSetup);
        context.getModEventBus().addListener(this::serverSetup);
    }
    
    /**
     * This client setup event
     */
    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MinecraftCapes.onEnable();
            
            //Try turn on capes
            Minecraft.getInstance().options.toggleModelPart(PlayerModelPart.CAPE, true);
            
            //Register the events
            MinecraftForge.EVENT_BUS.register(ClientForgeEvents.class);
            MinecraftForge.EVENT_BUS.register(ClientModEvents.class);
            
            MinecraftCapes.getLogger().info("Initialised");
        });
    }
    
    /**
     * This client setup event
     */
    @SubscribeEvent
    public void serverSetup(FMLDedicatedServerSetupEvent event) {
        event.enqueueWork(() -> {
            //Register the events
            MinecraftForge.EVENT_BUS.register(ServerModEvents.class);
        });
    }
}