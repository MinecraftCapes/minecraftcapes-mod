package net.minecraftcapes.mixin.common;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.friends.FriendsListActions;
import net.minecraft.client.gui.screens.social.PlayerOptionsScreen;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraftcapes.player.DownloadManager;
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

    @Shadow
    @Final
    private AvatarRenderState playerRenderState;

    @Shadow
    @Final
    protected UUID playerId;

    @Shadow
    @Final
    private String playerName;

    /** Starts downloading the selected profile before its preview is rendered. */
    @Inject(method = "<init>", at = @At("RETURN"))
    public void minecraftcapes$init(Screen lastScreen, UUID playerId, String playerName, FriendsListActions friendsListActions, Supplier skinGetter, boolean skinReportable, boolean chatReportable, boolean hasRecentMessages, CallbackInfo ci) {
        PlayerHandler playerHandler = PlayerHandler.get(
                playerId,
                playerName
        );

        DownloadManager.prepareDownload(playerHandler);
    }

    /** Applies the profile cosmetics to the avatar render state. */
    @Inject(method = "extractRenderState", at = @At(value = "RETURN"))
    public void minecraftcapes$extractRenderState(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        PlayerHandler playerHandler = PlayerHandler.get(playerId, playerName);
        playerHandler.extractRenderState(playerRenderState);
    }
}
