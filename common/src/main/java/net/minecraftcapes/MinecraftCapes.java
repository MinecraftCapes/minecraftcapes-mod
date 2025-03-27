package net.minecraftcapes;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.Getter;
import net.minecraft.SharedConstants;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.TriState;
import net.minecraftcapes.config.MinecraftCapesConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MinecraftCapes {
    
    public static final String MOD_ID = "minecraftcapes";
    public static final String MOD_NAME = "MinecraftCapes";
    public static final String MINECRAFT_VERSION = SharedConstants.getCurrentVersion().getName();

    @Getter
    private static final Logger logger = LogManager.getLogger(MOD_NAME);
    @Getter
    protected static RenderType capeGlint = RenderType.create("cape_glint",
            1536,
            RenderPipelines.GLINT,
            RenderType.CompositeState.builder()
                    .setTextureState(
                            new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ITEM, TriState.TRUE, false))
                    .setTexturingState(RenderType.ENTITY_GLINT_TEXTURING)
                    .setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING)
                    .createCompositeState(false));
    
    public static void onEnable() {
        MinecraftCapes.getLogger().info("Initialising");
        
        //Loading Config
        MinecraftCapesConfig.loadConfig();
    }
}
