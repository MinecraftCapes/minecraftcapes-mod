package net.minecraftcapes.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MinecraftCapesMixinPlugin implements IMixinConfigPlugin {
    
    private static Boolean isLunar() {
        try {
            Class.forName("com.moonsworth.lunar.genesis.Genesis");
            return true;
        } catch (Throwable ignored) {
        }
        
        return false;
    }
    
    @Override
    public void onLoad(String s) {
    }
    
    @Override
    public String getRefMapperConfig() {
        return null;
    }
    
    @Override
    public boolean shouldApplyMixin(String target, String mixin) {
        boolean lunar = isLunar();
        
        // Anything in your lunar package only applies on Lunar
        if (mixin.contains("lunar.")) {
            return lunar;
        }
        
        // Disable fragile vanilla mixins when running on Lunar
        if (mixin.contains("vanilla.")) {
            return !lunar;
        }
        
        return true;
    }
    
    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {}
    
    @Override
    public List<String> getMixins() {
        return null;
    }
    
    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}
    
    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}
    
}