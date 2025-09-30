package net.minecraftcapes.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(PlayerInfo.class)
public class MixinPlayerInfo {

    @Shadow
    @Final
    private GameProfile profile;

    @Inject(method = "createSkinLookup", at = @At("HEAD"))
    private static void createSkinLookup(GameProfile profile, CallbackInfoReturnable<Supplier<PlayerSkin>> cir) {
        DownloadManager.prepareDownload(profile.id(), profile.name(), false);
    }
    
    @Inject(method = "getSkin", at = @At("TAIL"), cancellable = true)
    public void getSkin(CallbackInfoReturnable<PlayerSkin> cir) {
        PlayerHandler playerHandler = PlayerHandler.get(profile.id());

        //Check player handler is loaded
        if(playerHandler.getHasInfo()) {
            //Set initial values
            PlayerSkin playerSkin = cir.getReturnValue();
            ClientAsset.Texture capeTexture = playerSkin.cape();
            ClientAsset.Texture elytraTexture = playerSkin.elytra();

            //If we have a cape, lets load it
            if(MinecraftCapesConfig.isCapeVisible() && playerHandler.getCapeLocation() != null) {
                capeTexture = new ClientAsset.ResourceTexture(playerHandler.getCapeLocation(), playerHandler.getCapeLocation());
                elytraTexture = capeTexture;
            }

            //Return new player skin
            PlayerSkin newPlayerSkin = new PlayerSkin(
                    playerSkin.body(),
                    capeTexture, elytraTexture,
                    playerSkin.model(), playerSkin.secure()
            );
            cir.setReturnValue(newPlayerSkin);
        }
    }
    
}
