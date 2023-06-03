package net.minecraftcapes.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraftcapes.player.DownloadManager;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener implements ClientGamePacketListener {

    @Shadow
    @Final
    private Map<UUID, PlayerInfo> playerInfoMap;
    
    @Inject(method = "handlePlayerInfo", at = @At("RETURN"))
    public void handlePlayerInfo(ClientboundPlayerInfoPacket infoPacket, CallbackInfo ci) {
        if(infoPacket.getAction() == ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER) {
            infoPacket.getEntries().forEach(playerUpdate -> PlayerHandler.remove(playerUpdate.getProfile().getId()));
        } else {
            infoPacket.getEntries().forEach(entry -> {
                PlayerInfo playerInfo = this.playerInfoMap.get(entry.getProfile().getId());
                DownloadManager.prepareDownload(playerInfo, false);
            });
        }
    }
}
