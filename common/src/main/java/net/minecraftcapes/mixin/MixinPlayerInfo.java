package net.minecraftcapes.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
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
        DownloadManager.prepareDownload(profile.getId(), profile.getName(), false);
    }
    
    @Inject(method = "getSkin", at = @At("TAIL"), cancellable = true)
    public void getSkin(CallbackInfoReturnable<PlayerSkin> cir) {
        PlayerHandler playerHandler = PlayerHandler.get(profile.getId());

        //Check player handler is loaded
        if(playerHandler.getHasInfo()) {
            //Set initial values
            PlayerSkin playerSkin = cir.getReturnValue();
            ResourceLocation capeTexture = playerSkin.capeTexture();
            ResourceLocation elytraTexture = playerSkin.elytraTexture();

            //If we have a cape, lets load it
            if(MinecraftCapesConfig.isCapeVisible() && playerHandler.getCapeLocation() != null) {
                capeTexture = playerHandler.getCapeLocation();
                elytraTexture = playerHandler.getCapeLocation();
            }

            //Return new player skin
            PlayerSkin newPlayerSkin = new PlayerSkin(
                    playerSkin.texture(), playerSkin.textureUrl(),
                    capeTexture, elytraTexture,
                    playerSkin.model(), playerSkin.secure()
            );
            cir.setReturnValue(newPlayerSkin);
        }
    }
    
}
