package net.minecraftcapes.forge;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.forge.events.KeyHandlerEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

@Mod(MinecraftCapes.MOD_ID)
public class ForgeImplementation extends MinecraftCapes {
    public static KeyMapping KEY_MAPPING;
    
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
        
        //Map the Key
        KEY_MAPPING = new KeyMapping("key.minecraftcapes.gui", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_J, "category.minecraftcapes.gui");
        
        //Try turn on capes
        Minecraft.getInstance().options.toggleModelPart(PlayerModelPart.CAPE, true);
        
        MinecraftCapes.getLogger().info("Initialised");
    }
}
