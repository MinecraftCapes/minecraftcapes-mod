package net.minecraftcapes.mixin;

import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.entity.ClientMannequin;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.level.Level;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientMannequin.class)
public abstract class ClientMannequinMixin extends Mannequin implements ClientAvatarEntity {

    public ClientMannequinMixin(EntityType<Mannequin> p_446465_, Level p_446512_) {
        super(p_446465_, p_446512_);
    }

    @Inject(method = "updateSkin", at = @At("HEAD"))
    public void updateSkin(CallbackInfo ci) {
        PlayerHandler playerHandler = PlayerHandler.get(this);
        DownloadManager.prepareDownload(playerHandler);
    }

    @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
    public void getSkin(CallbackInfoReturnable<PlayerSkin> cir) {
        PlayerHandler playerHandler = PlayerHandler.get(this);

        //Check player handler is loaded
        if(playerHandler.getHasInfo()) {
            cir.setReturnValue(playerHandler.getSkin(cir.getReturnValue()));
        }
    }
}
