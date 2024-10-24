package net.minecraftcapes.mixin;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WingsLayer.class)
public abstract class MixinElytraLayer<S extends HumanoidRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {

    public MixinElytraLayer(RenderLayerParent<S, M> renderer) {
        super(renderer);
    }

    @Inject(method = "getPlayerElytraTexture", at = @At("HEAD"), cancellable = true)
    private static void getPlayerElytraTexture(HumanoidRenderState player, CallbackInfoReturnable<ResourceLocation> cir) {
        if (player instanceof PlayerRenderState playerrenderstate) {
            PlayerHandler playerHandler = PlayerHandler.get(((PlayerRenderState) player).name);
            if(!playerHandler.getForceShowElytra() && playerHandler.getForceHideElytra()) {
                cir.setReturnValue(null);
                return;
            }

            if (playerHandler.getCapeLocation() != null && MinecraftCapesConfig.isCapeVisible()) {
                cir.setReturnValue(playerHandler.getCapeLocation());
                return;
            }

            PlayerSkin playerskin = playerrenderstate.skin;
            if (playerskin.elytraTexture() != null) {
                cir.setReturnValue(playerskin.elytraTexture());
                return;
            }

            if (playerskin.capeTexture() != null && playerrenderstate.showCape) {
                cir.setReturnValue(playerskin.capeTexture());
                return;
            }
        }

        cir.setReturnValue(null);
    }
}
