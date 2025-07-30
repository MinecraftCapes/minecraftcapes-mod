package net.minecraftcapes.mixin;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.RenderType.*;

@Mixin(RenderType.class)
public abstract class MixinRenderType extends RenderStateShard {

    private static RenderType TEST;

    @Mixin(RenderType.Com)
    abstract class CompositeRenderType {}

    @Shadow
    private CompositeRenderType create(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, CompositeState state)

    public MixinRenderType(String name, Runnable setupState, Runnable clearState) {
        super(name, setupState, clearState);
    }

    @Inject(method = "<clinit>", at = @At(value = "HEAD"))
    public void NewRenderType(CallbackInfo ci) {
        TEST = create("solid", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 4194304, true, false, RenderType.CompositeState.builder().setLightmapState(LIGHTMAP).setShaderState(RENDERTYPE_SOLID_SHADER).setTextureState(BLOCK_SHEET_MIPPED).createCompositeState(true));
    }
}
