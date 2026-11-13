package net.minecraftcapes.mixin.common;

import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.entity.ClientMannequin;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.level.Level;
import net.minecraftcapes.player.DownloadManager;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientMannequin.class)
public abstract class MixinClientMannequin extends Mannequin implements ClientAvatarEntity {

    /** Initializes the vanilla mannequin base used by this mixin. */
    public MixinClientMannequin(EntityType<Mannequin> type, Level level) {
        super(type, level);
    }

    /** Starts the cosmetic download when a mannequin profile is updated. */
    @Inject(method = "updateSkin", at = @At("RETURN"))
    public void minecraftcapes$updateSkin(CallbackInfo ci) {
        PlayerHandler playerHandler = PlayerHandler.get(
                this.getProfile().partialProfile().id(),
                this.getProfile().partialProfile().name()
        );

        DownloadManager.prepareDownload(playerHandler);
    }

    /** Adds loaded cosmetics to the mannequin skin, or starts its download. */
    @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
    public void minecraftcapes$getSkin(CallbackInfoReturnable<PlayerSkin> cir) {
        PlayerHandler playerHandler = PlayerHandler.get(
                this.getProfile().partialProfile().id(),
                this.getProfile().partialProfile().name()
        );

        DownloadManager.prepareDownload(playerHandler);
        cir.setReturnValue(playerHandler.getSkin(cir.getReturnValue()));
    }
}