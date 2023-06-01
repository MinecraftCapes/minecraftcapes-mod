package net.minecraftcapes.mixin;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import net.minecraftcapes.player.CapeGlintManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.SortedMap;

@Mixin(RenderBuffers.class)
public class MixinRenderBuffers {

    @Shadow
    @Final
    private SortedMap<RenderType, BufferBuilder> fixedBuffers;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        if(CapeGlintManager.CAPE_GLINT != null) {
            fixedBuffers.put(CapeGlintManager.CAPE_GLINT, new BufferBuilder(CapeGlintManager.CAPE_GLINT.bufferSize()));
        }
    }

}
