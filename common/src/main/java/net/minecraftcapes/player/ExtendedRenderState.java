package net.minecraftcapes.player;

import net.minecraft.resources.ResourceLocation;

public interface ExtendedRenderState {
    
    void minecraftcapes$setCapeEnabled(boolean value);
    
    boolean minecraftcapes$getCapeEnabled();
    
    void minecraftcapes$setGapeGlint(boolean value);
    
    boolean minecraftcapes$hasCapeGlint();
    
    void minecraftcapes$setEarsTexture(ResourceLocation value);
    
    ResourceLocation minecraftcapes$getEarsTexture();
    
}
