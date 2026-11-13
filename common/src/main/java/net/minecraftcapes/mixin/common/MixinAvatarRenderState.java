package net.minecraftcapes.mixin.common;

import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import net.minecraftcapes.player.ExtendedRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AvatarRenderState.class)
public class MixinAvatarRenderState implements ExtendedRenderState {

    @Unique
    private boolean minecraftcapes$capeEnabled;

    @Unique
    private boolean minecraftcapes$capeGlint;

    @Unique
    private boolean minecraftcapes$earsEnabled;

    @Unique
    private Identifier minecraftcapes$earsTexture;

    /** Sets whether the custom cape rendering path is enabled. */
    @Override
    public void minecraftcapes$setCapeEnabled(boolean value) {
        this.minecraftcapes$capeEnabled = value;
    }

    /** Returns whether the custom cape rendering path is enabled. */
    @Override
    public boolean minecraftcapes$getCapeEnabled() {
        return this.minecraftcapes$capeEnabled;
    }

    /** Sets the cape glint flag; retains the existing interface method name. */
    @Override
    public void minecraftcapes$setGapeGlint(boolean value) {
        this.minecraftcapes$capeGlint = value;
    }

    /** Returns whether the cape should render with glint. */
    @Override
    public boolean minecraftcapes$hasCapeGlint() {
        return this.minecraftcapes$capeGlint;
    }

    /** Sets whether custom ear rendering is enabled. */
    @Override
    public void minecraftcapes$setEarsEnabled(boolean value) {
        this.minecraftcapes$earsEnabled = value;
    }

    /** Returns whether custom ear rendering is enabled. */
    @Override
    public boolean minecraftcapes$getEarsEnabled() {
        return this.minecraftcapes$earsEnabled;
    }

    /** Stores the custom ear texture for this render state. */
    @Override
    public void minecraftcapes$setEarsTexture(Identifier value) {
        this.minecraftcapes$earsTexture = value;
    }

    /** Returns the custom ear texture, or null when none is loaded. */
    @Override
    public Identifier minecraftcapes$getEarsTexture() {
        return this.minecraftcapes$earsTexture;
    }
}
