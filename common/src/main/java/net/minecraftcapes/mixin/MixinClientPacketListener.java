package net.minecraftcapes.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
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
public abstract class MixinClientPacketListener implements TickablePacketListener, ClientGamePacketListener {

    @Shadow
    @Final
    private Map<UUID, PlayerInfo> playerInfoMap;

    @Inject(method = "handlePlayerInfoUpdate", at = @At("RETURN"))
    public void handlePlayerInfoUpdate(ClientboundPlayerInfoUpdatePacket updatePacket, CallbackInfo ci) {
        updatePacket.newEntries().forEach(entry -> {
            PlayerInfo playerInfo = this.playerInfoMap.get(entry.profileId());
            DownloadManager.prepareDownload(playerInfo, false);
        });
    }

    @Inject(method = "handlePlayerInfoRemove", at = @At("RETURN"))
    public void handlePlayerInfoRemove(ClientboundPlayerInfoRemovePacket removePacket, CallbackInfo ci) {
        removePacket.profileIds().forEach(PlayerHandler::remove);
    }

}
