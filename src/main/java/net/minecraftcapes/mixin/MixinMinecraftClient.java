package net.minecraftcapes.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.gui.MenuScreen;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraftClient {

    @Shadow
    public Screen currentScreen;

    @Shadow
    public abstract void setScreen(Screen screen);

    KeyBinding keyBinding = new KeyBinding("Open MinecraftCapes", Keyboard.KEY_J);

    @Inject(method = "tick", at = @At("TAIL"))
    public void minecraftcapes$tick(CallbackInfo ci) {
        if(!MinecraftCapes.getScheduledTasks().isEmpty()) {
            Runnable runnable = MinecraftCapes.getScheduledTasks().remove(0);
            runnable.run();
        }

        if(Keyboard.isKeyDown(keyBinding.code)) {
            if(this.currentScreen == null) {
                this.setScreen(new MenuScreen());
            }
        }
    }

}
