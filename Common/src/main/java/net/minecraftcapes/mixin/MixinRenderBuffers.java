package net.minecraftcapes.mixin;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import net.minecraftcapes.player.CapeGlintManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderBuffers.class)
public class MixinRenderBuffers {
    
    @Inject(method = "put", at = @At("RETURN"))
    private static void put(Object2ObjectLinkedOpenHashMap<RenderType, ByteBufferBuilder> map, RenderType renderType, CallbackInfo ci) {
        if(CapeGlintManager.CAPE_GLINT != null && !map.containsKey(CapeGlintManager.CAPE_GLINT)) {
            map.put(CapeGlintManager.CAPE_GLINT, new ByteBufferBuilder(CapeGlintManager.CAPE_GLINT.bufferSize()));
        }
    }
}
