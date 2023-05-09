package net.minecraftcapes.player.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class CapeGlintManager {
    
    public static RenderType CAPE_GLINT;
    
    //https://github.com/IrisShaders/Iris/issues/618
    
    public static VertexConsumer getCapeBuffer(MultiBufferSource bufferSource, RenderType renderType, boolean hasGlint) {
        return hasGlint ? VertexMultiConsumer.create(bufferSource.getBuffer(CAPE_GLINT), bufferSource.getBuffer(renderType)) : bufferSource.getBuffer(renderType);
    }
}
