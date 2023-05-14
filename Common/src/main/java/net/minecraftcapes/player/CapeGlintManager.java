package net.minecraftcapes.player;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraftcapes.MinecraftCapes;

public class CapeGlintManager {

    public static RenderType CAPE_GLINT = MinecraftCapes.getCapeGlint();

    public static VertexConsumer getCapeBuffer(MultiBufferSource bufferSource, RenderType renderType, boolean hasGlint) {
        return hasGlint ? VertexMultiConsumer.create(bufferSource.getBuffer(CAPE_GLINT), bufferSource.getBuffer(renderType)) : bufferSource.getBuffer(renderType);
    }

    public static VertexConsumer getArmorFoilBuffer(MultiBufferSource $$0, RenderType $$1, boolean $$2, boolean $$3) {
        return $$3 ? VertexMultiConsumer.create($$0.getBuffer($$2 ? RenderType.armorGlint() : RenderType.armorEntityGlint()), $$0.getBuffer($$1)) : $$0.getBuffer($$1);
    }
}
