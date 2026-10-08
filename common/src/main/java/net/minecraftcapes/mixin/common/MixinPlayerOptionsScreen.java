package net.minecraftcapes.mixin.common;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.friends.FriendsListActions;
import net.minecraft.client.gui.screens.social.PlayerOptionsScreen;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;
import net.minecraftcapes.player.ExtendedRenderState;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;
import java.util.function.Supplier;

@Mixin(PlayerOptionsScreen.class)
public abstract class MixinPlayerOptionsScreen {
    
    @Shadow
    @Final
    private AvatarRenderState playerRenderState;
    
    @Shadow
    @Final
    protected UUID playerId;
    
    @Inject(method = "<init>", at = @At("RETURN"))
    public void minecraftcapes$init(Screen lastScreen, UUID playerId, String playerName, FriendsListActions friendsListActions, Supplier skinGetter, boolean skinReportable, boolean chatReportable, boolean hasRecentMessages, CallbackInfo ci) {
        PlayerHandler playerHandler = PlayerHandler.get(playerId);
        playerHandler.setName(playerName);
        
        // Force a download
        if(!playerHandler.getHasInfo()) {
            DownloadManager.prepareDownload(playerHandler);
        }
    }
    
    @Inject(method = "extractRenderState", at = @At(value = "RETURN"))
    public void minecraftcapes$extractRenderState(GuiGraphicsExtractor graphics, int xm, int ym, float a, CallbackInfo ci) {
        ExtendedRenderState extendedRenderState = (ExtendedRenderState) playerRenderState;
        PlayerHandler playerHandler = PlayerHandler.get(playerId);
  
        if(playerHandler.getHasInfo()) {
            playerRenderState.skin = playerHandler.getSkin(playerRenderState.skin);
            
            playerRenderState.isUpsideDown = playerHandler.isUpsideDown();
            
            // We do a double check here because of the @WrapCondition
            // if isHasEars is true but isEarsVisible is false it will render with vanilla
            // The double check ensure we only render ears if they're visible
            playerRenderState.showExtraEars = playerRenderState.showExtraEars || (playerHandler.isHasEars() && MinecraftCapesConfig.isEarsVisible());
            
            extendedRenderState.minecraftcapes$setCapeEnabled(MinecraftCapesConfig.isCapeVisible());
            extendedRenderState.minecraftcapes$setGapeGlint(playerHandler.getHasCapeGlint());
            extendedRenderState.minecraftcapes$setEarsEnabled(MinecraftCapesConfig.isEarsVisible());
            extendedRenderState.minecraftcapes$setEarsTexture(playerHandler.getEarLocation());
        }
    }
}
