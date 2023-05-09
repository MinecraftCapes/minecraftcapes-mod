package net.minecraftcapes;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraftcapes.compatability.ArmorVisibilityHook;
import net.minecraftcapes.compatability.OriginsHook;
import net.minecraftcapes.compatability.TrinketsHook;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.gui.MenuScreen;
import net.minecraftcapes.player.CapeGlintManager;
import org.lwjgl.glfw.GLFW;

public class MinecraftCapes implements ClientModInitializer,MinecraftCapesCommon {

	private static KeyMapping keyBinding;
	
	@Override
	public void onInitializeClient() {
        //Set API
        MinecraftCapesConstants.API = this;
        
        MinecraftCapesConstants.LOG.info("Initialising");
        
		//Loading Config
		MinecraftCapesConfig.loadConfig();

		//Configure the KeyBind
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
			"key.minecraftcapes.gui",
            InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_J,
			"category.minecraftcapes.gui"
		));

		//React to key pressed
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while(keyBinding.isDown()) {
				Minecraft.getInstance().setScreen(new MenuScreen());
			}
		});

		//Do Mod compatibility checks :(
		if(doesClassExist("dev.emi.trinkets.TrinketsMain")) {
			new TrinketsHook();
		}

		if(doesClassExist("com.trikzon.armor_visibility.ArmorVisibility")) {
			new ArmorVisibilityHook();
		}

		if(doesClassExist("io.github.apace100.origins.Origins")) {
			new OriginsHook();
		}
        
        MinecraftCapesConstants.LOG.info("Initialised");
	}

	/**
	 * Checks if a class exists or not
	 * @param name
	 * @return
	 */
	private boolean doesClassExist(String name) {
		try {
			Class c = Class.forName(name);
			System.out.println(c);
			if (c != null) {
				return true;
			}
		} catch (ClassNotFoundException e) {}
		return false;
	}
    
    @Override
    public RenderType capeGlint() {
        return RenderType.create("cape_glint",
                DefaultVertexFormat.POSITION_TEX,
                VertexFormat.Mode.QUADS,
                256,
                RenderType.CompositeState.builder()
                        .setShaderState(RenderType.RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER)
                        .setTextureState(
                                new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ITEM, true, false))
                        .setWriteMaskState(RenderType.COLOR_WRITE)
                        .setCullState(RenderType.NO_CULL)
                        .setDepthTestState(RenderType.EQUAL_DEPTH_TEST)
                        .setTransparencyState(RenderType.GLINT_TRANSPARENCY)
                        .setTexturingState(RenderType.ENTITY_GLINT_TEXTURING)
                        .setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING)
                        .createCompositeState(false)
        );
    }
    
}
