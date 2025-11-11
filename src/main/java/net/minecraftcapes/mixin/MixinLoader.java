package net.minecraftcapes.mixin;

import cpw.mods.modlauncher.api.ITransformer;
import net.minecraftforge.forgespi.coremod.ICoreModFile;
import net.minecraftforge.forgespi.coremod.ICoreModProvider;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Collections;
import java.util.List;

public class MixinLoader implements ICoreModProvider {

    public MixinLoader() {
        MixinBootstrap.init();
        Mixins.addConfiguration("minecraftcapes.mixins.json");
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
    }

    @Override
    public void addCoreMod(ICoreModFile iCoreModFile) {

    }

    @Override
    public List<ITransformer<?>> getCoreModTransformers() {
        return Collections.emptyList();
    }
}