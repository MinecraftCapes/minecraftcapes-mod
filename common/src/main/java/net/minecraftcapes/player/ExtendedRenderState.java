package net.minecraftcapes.player;

import net.minecraft.resources.Identifier;

public interface ExtendedRenderState {
    
    void minecraftcapes$setCapeEnabled(boolean value);
    
    boolean minecraftcapes$getCapeEnabled();
    
    void minecraftcapes$setGapeGlint(boolean value);
    
    boolean minecraftcapes$hasCapeGlint();
    
    void minecraftcapes$setEarsEnabled(boolean value);
    
    boolean minecraftcapes$getEarsEnabled();
    
    void minecraftcapes$setEarsTexture(Identifier value);
    
    Identifier minecraftcapes$getEarsTexture();
    
}
