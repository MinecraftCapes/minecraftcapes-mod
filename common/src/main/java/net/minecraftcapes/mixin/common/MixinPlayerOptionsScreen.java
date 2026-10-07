package net.minecraftcapes.mixin.common;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.friends.FriendsListActions;
import net.minecraft.client.gui.screens.social.PlayerOptionsScreen;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
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

import java.util.UUID;
import java.util.function.Supplier;

@Mixin(PlayerOptionsScreen.class)
public abstract class MixinPlayerOptionsScreen {
    
    @Final
    @Shadow
    private AvatarRenderState playerRenderState;
    
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void minecraftcapes$init(Screen lastScreen, UUID playerId, String playerName, FriendsListActions friendsListActions, Supplier skinGetter, boolean skinReportable, boolean chatReportable, boolean hasRecentMessages, CallbackInfo ci) {
        ExtendedRenderState extendedRenderState = (ExtendedRenderState) this.playerRenderState;
        PlayerHandler playerHandler = PlayerHandler.get(playerId);
        playerHandler.setName(playerName);
        
        System.out.println("woo1");
        if(playerHandler.getHasInfo()) {
            System.out.println("woo2");
            this.playerRenderState.isUpsideDown = playerHandler.isUpsideDown();
            
            // We do a double check here because of the @WrapCondition
            // if isHasEars is true but isEarsVisible is false it will render with vanilla
            // The double check ensure we only render ears if they're visible
            this.playerRenderState.showExtraEars = this.playerRenderState.showExtraEars || (playerHandler.isHasEars() && MinecraftCapesConfig.isEarsVisible());
            
            extendedRenderState.minecraftcapes$setCapeEnabled(MinecraftCapesConfig.isCapeVisible());
            extendedRenderState.minecraftcapes$setGapeGlint(playerHandler.getHasCapeGlint());
            extendedRenderState.minecraftcapes$setEarsEnabled(MinecraftCapesConfig.isEarsVisible());
            extendedRenderState.minecraftcapes$setEarsTexture(playerHandler.getEarLocation());
        } else {
            DownloadManager.prepareDownload(playerHandler);
        }
    }
}
