package net.minecraftcapes.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MinecraftCapesMixinPlugin implements IMixinConfigPlugin {

    /** Detects the Lunar client to select compatible ear model mixins. */
    private static Boolean isLunar() {
        try {
            Class.forName("com.moonsworth.lunar.genesis.Genesis");
            return true;
        } catch (Throwable ignored) {
        }

        return false;
    }

    /** Leaves mixin initialization to the standard configuration. */
    @Override
    public void onLoad(String s) {
    }

    /** Uses the default reference mapper configuration. */
    @Override
    public String getRefMapperConfig() {
        return null;
    }

    /** Selects Lunar or vanilla mixins while always enabling common mixins. */
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

    /** Keeps the target classes supplied by the mixin configuration. */
    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {}

    /** Uses only the mixins declared in the configuration file. */
    @Override
    public List<String> getMixins() {
        return null;
    }

    /** Leaves the target bytecode unchanged before mixin application. */
    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}

    /** Leaves the target bytecode unchanged after mixin application. */
    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}

}