package net.minecraftcapes.mixin;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraftcapes.MinecraftCapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderBuffers.class)
public class MixinRenderBuffers {
    
    @Inject(method = "put", at = @At("RETURN"))
    private static void put(Object2ObjectLinkedOpenHashMap<RenderType, ByteBufferBuilder> map, RenderType renderType, CallbackInfo ci) {
        RenderType CAPE_GLINT = MinecraftCapes.getCapeGlint();
        if(CAPE_GLINT != null && !map.containsKey(CAPE_GLINT)) {
            map.put(CAPE_GLINT, new ByteBufferBuilder(CAPE_GLINT.bufferSize()));
        }
    }
}
