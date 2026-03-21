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
    
    @Override
    public void minecraftcapes$setCapeEnabled(boolean value) {
        this.minecraftcapes$capeEnabled = value;
    }
    
    @Override
    public boolean minecraftcapes$getCapeEnabled() {
        return this.minecraftcapes$capeEnabled;
    }
    
    @Override
    public void minecraftcapes$setGapeGlint(boolean value) {
        this.minecraftcapes$capeGlint = value;
    }
    
    @Override
    public boolean minecraftcapes$hasCapeGlint() {
        return this.minecraftcapes$capeGlint;
    }
    
    @Override
    public void minecraftcapes$setEarsEnabled(boolean value) {
        this.minecraftcapes$earsEnabled = value;
    }
    
    @Override
    public boolean minecraftcapes$getEarsEnabled() {
        return this.minecraftcapes$earsEnabled;
    }
    
    @Override
    public void minecraftcapes$setEarsTexture(Identifier value) {
        this.minecraftcapes$earsTexture = value;
    }
    
    @Override
    public Identifier minecraftcapes$getEarsTexture() {
        return this.minecraftcapes$earsTexture;
    }
    
}
