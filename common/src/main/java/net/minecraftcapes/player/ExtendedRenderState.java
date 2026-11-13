package net.minecraftcapes.player;

import net.minecraft.resources.Identifier;

public interface ExtendedRenderState {

    /** Sets whether the custom cape rendering path is enabled. */
    void minecraftcapes$setCapeEnabled(boolean value);

    /** Returns whether the custom cape rendering path is enabled. */
    boolean minecraftcapes$getCapeEnabled();

    /** Sets cape glint, retaining the existing interface method name. */
    void minecraftcapes$setGapeGlint(boolean value);

    /** Returns whether the cape should render with glint. */
    boolean minecraftcapes$hasCapeGlint();

    /** Sets whether custom ear rendering is enabled. */
    void minecraftcapes$setEarsEnabled(boolean value);

    /** Returns whether custom ear rendering is enabled. */
    boolean minecraftcapes$getEarsEnabled();

    /** Stores the custom ear texture, or null when none is loaded. */
    void minecraftcapes$setEarsTexture(Identifier value);

    /** Returns the custom ear texture, or null when none is loaded. */
    Identifier minecraftcapes$getEarsTexture();

}
