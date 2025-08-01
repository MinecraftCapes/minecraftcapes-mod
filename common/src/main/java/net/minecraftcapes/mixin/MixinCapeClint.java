package net.minecraftcapes.mixin;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraftcapes.player.CapeGlintManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderType.class)
public class MixinCapeClint extends RenderStateShard {

    public MixinCapeClint(String name, Runnable setupState, Runnable clearState) {
        super(name, setupState, clearState);
    }

    @Inject(method = "<clinit>", at = @At(value = "HEAD"))
    private static void addCapeGlint(CallbackInfo ci) {
        CapeGlintManager.CAPE_GLINT = call_create("cape_glint",
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

    @Invoker(value = "create")
    static RenderType.CompositeRenderType call_create(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, RenderType.CompositeState state) {
        throw new IllegalStateException("Failed to add CapeGlint");
    }
}
