package net.minecraftcapes.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import net.minecraftcapes.MinecraftCapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.Arrays;

@Mixin(GameOptions.class)
public class GameOptionMixin {

    @Shadow
    public KeyBinding[] keyBindings;

    @Inject(method = "<init>(Lnet/minecraft/client/Minecraft;Ljava/io/File;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/options/GameOptions;load()V"))
    private void minecraftcapes$registerKeyBind(Minecraft minecraft, File dir, CallbackInfo ci) {
        keyBindings = Arrays.copyOf(keyBindings, keyBindings.length + 1);
        keyBindings[keyBindings.length - 1] = MinecraftCapes.keyBinding;

    }
}
