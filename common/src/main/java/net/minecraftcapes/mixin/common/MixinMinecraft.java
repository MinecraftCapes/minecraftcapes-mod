package net.minecraftcapes.mixin.common;

import net.minecraft.client.Minecraft;
import net.minecraftcapes.player.DownloadManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    
    /** Releases session-owned textures while the client renderer and texture manager are still alive. */
    @Inject(method = "close", at = @At("HEAD"))
    private void minecraftcapes$shutdown(CallbackInfo ci) {
        DownloadManager.shutdown();
    }
}
