package net.minecraftcapes.player;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraftcapes.MinecraftCapes;

public class CapeGlintManager {
    public static RenderType CAPE_GLINT = MinecraftCapes.getCapeGlint();
    public static VertexConsumer getCapeBuffer(MultiBufferSource bufferSource, RenderType renderType) {
        return VertexMultiConsumer.create(bufferSource.getBuffer(CAPE_GLINT), bufferSource.getBuffer(renderType));
    }
}
