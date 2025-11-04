package net.minecraftcapes;

import com.mojang.blaze3d.platform.InputConstants;
import lombok.Getter;
import net.minecraft.SharedConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.rendertype.*;
import net.minecraft.resources.Identifier;
import net.minecraftcapes.config.MinecraftCapesConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Lazy;
import org.lwjgl.glfw.GLFW;

public class MinecraftCapes {
    
    public static final String MOD_ID = "minecraftcapes";
    public static final String MOD_NAME = "MinecraftCapes";
    public static final String MINECRAFT_VERSION = SharedConstants.getCurrentVersion().name();

    @Getter
    private static final Logger logger = LogManager.getLogger(MOD_NAME);
    @Getter
    protected static RenderType capeGlint = RenderType.create("cape_glint",
            RenderSetup.builder(RenderPipelines.GLINT)
                    .withTexture("Sampler0", ItemRenderer.ENCHANTED_GLINT_ITEM)
                    .setTextureTransform(TextureTransform.ENTITY_GLINT_TEXTURING)
                    .createRenderSetup()
    );

    public static final Lazy<KeyMapping> KEY_MAPPING = Lazy.lazy(() -> new KeyMapping(
            "key.minecraftcapes.gui",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "gui"))
    ));
    
    public static void onEnable() {
        MinecraftCapes.getLogger().info("Initialising");
        
        //Loading Config
        MinecraftCapesConfig.loadConfig();
    }
}
