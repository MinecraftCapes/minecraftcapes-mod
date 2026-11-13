package net.minecraftcapes.mixin.common;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

    /** Clears disconnected profile state without unloading the session's shared textures. */
    @Inject(method = "close", at = @At("TAIL"))
    private void minecraftcapes$close(CallbackInfo ci) {
        PlayerHandler.clearAll();
    }

    /** Removes profile handlers while retaining shared textures for the rest of the session. */
    @Inject(method = "handlePlayerInfoRemove", at = @At("TAIL"))
    public void minecraftcapes$handlePlayerInfoRemove(ClientboundPlayerInfoRemovePacket packet, CallbackInfo ci) {
        for (UUID profileId : packet.profileIds()) {
            PlayerHandler.remove(profileId);
        }
    }
}
