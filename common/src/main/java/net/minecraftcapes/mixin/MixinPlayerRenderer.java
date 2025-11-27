package net.minecraftcapes.mixin;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class MixinPlayerRenderer {
    
    @Inject(method = "extractRenderState(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;F)V", at = @At(value = "TAIL"))
    public void extractRenderState(AbstractClientPlayer abstractClientPlayer, PlayerRenderState playerRenderState, float p_364121_, CallbackInfo ci) {
        ExtendedRenderState minecraftcapes$playerRenderState = (ExtendedRenderState) playerRenderState;
        PlayerHandler playerHandler = PlayerHandler.get(abstractClientPlayer.getUUID());
        
        playerRenderState.isUpsideDown = playerHandler.isUpsideDown();
        minecraftcapes$playerRenderState.minecraftcapes$setCapeEnabled(MinecraftCapesConfig.isCapeVisible());
        minecraftcapes$playerRenderState.minecraftcapes$setGapeGlint(playerHandler.getHasCapeGlint());
    }
}
