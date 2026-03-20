package net.minecraftcapes.mixin.common;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.ExtendedRenderState;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerRenderer extends LivingEntityRenderer<AbstractClientPlayer, PlayerRenderState, PlayerModel> {
    
    public MixinPlayerRenderer(EntityRendererProvider.Context p_174289_, PlayerModel p_174290_, float p_174291_) {
        super(p_174289_, p_174290_, p_174291_);
    }
    
    @Inject(method = "extractRenderState(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;F)V", at = @At(value = "TAIL"))
    public void extractRenderState(AbstractClientPlayer abstractClientPlayer, PlayerRenderState playerRenderState, float p_445702_, CallbackInfo ci) {
        ExtendedRenderState extendedRenderState = (ExtendedRenderState) playerRenderState;
        PlayerHandler playerHandler = PlayerHandler.get(abstractClientPlayer.getUUID());
        
        if(playerHandler.getHasInfo()) {
            playerRenderState.isUpsideDown = playerHandler.isUpsideDown();
            extendedRenderState.minecraftcapes$setEarsEnabled(MinecraftCapesConfig.isEarsVisible() && playerHandler.getEarLocation() != null);
            extendedRenderState.minecraftcapes$setCapeEnabled(MinecraftCapesConfig.isCapeVisible());
            extendedRenderState.minecraftcapes$setGapeGlint(playerHandler.getHasCapeGlint());
            extendedRenderState.minecraftcapes$setEarsTexture(playerHandler.getEarLocation());
        }
    }
}
