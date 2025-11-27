package net.minecraftcapes.mixin;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerRenderState.class)
public class MixinPlayerRenderState implements ExtendedRenderState {
    
    @Unique
    private boolean minecraftcapes$capeEnabled;
    
    @Unique
    private boolean minecraftcapes$capeGlint;
    
    @Unique
    private boolean minecraftcapes$earsEnabled;
    
    @Unique
    private ResourceLocation minecraftcapes$earsTexture;
    
    
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
    public void minecraftcapes$setEarsTexture(ResourceLocation value) {
        this.minecraftcapes$earsTexture = value;
    }
    
    @Override
    public ResourceLocation minecraftcapes$getEarsTexture() {
        return this.minecraftcapes$earsTexture;
    }
    
}
