package net.minecraftcapes.mixin;

import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.helpers.MinecraftApi;
import net.minecraftcapes.player.DownloadManager;
import net.minecraftcapes.player.PlayerHandler;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin extends LivingEntityRenderer {

    @Shadow
    private BipedEntityModel bipedModel;

    @Unique
    private PlayerHandler playerHandler;

    @Unique
    private boolean needsPlayerHandler = true;

    public PlayerEntityRendererMixin(EntityModel entityModel, float shadowRadius) {
        super(entityModel, shadowRadius);
    }

    /**
     * Gets the first playerhandler instance
     * @param playerEntity
     * @param e
     * @param f
     * @param g
     * @param h
     * @param par6
     * @param ci
     */
    @Inject(method = "render(Lnet/minecraft/entity/player/PlayerEntity;DDDFF)V", at = @At(value = "TAIL"))
    public void minecraftcapes$getPlayerHandler(PlayerEntity playerEntity, double e, double f, double g, float h, float par6, CallbackInfo ci) {
        if(needsPlayerHandler && playerHandler == null) {
            needsPlayerHandler = false;
            Thread prepareProfile = new Thread(() -> {
                UUID onlineUUID = MinecraftApi.getUUID(playerEntity.name);
                if(onlineUUID != null) {
                    playerHandler = PlayerHandler.get(onlineUUID);
                    DownloadManager.prepareDownload(onlineUUID, playerEntity.name, false);
                }
            });
            prepareProfile.start();
        }
    }

    /**
     * Even though this cancels both, not a massive issue as ears are only on deadmau5
     * @param instance
     * @param url
     * @param backup
     * @return
     */
    @Redirect(method = "renderMore(Lnet/minecraft/entity/player/PlayerEntity;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;bindDownloadedTexture(Ljava/lang/String;Ljava/lang/String;)Z"))
    public boolean minecraftcapes$defaultDownload(PlayerEntityRenderer instance, String url, String backup) {
        if(!MinecraftCapesConfig.isCapeVisible()) {
            return false;
        } else {
            return this.bindDownloadedTexture(url, backup);
        }
    }

    /**
     * Render Ears
     * @param playerEntity
     * @param f
     * @param ci
     */
    @Inject(method = "renderMore(Lnet/minecraft/entity/player/PlayerEntity;F)V", at = @At(value = "TAIL"))
    public void minecraftcapes$renderEars(PlayerEntity playerEntity, float f, CallbackInfo ci) {
        if(playerHandler != null && playerHandler.getEarLocation() >= 0 && MinecraftCapesConfig.isEarsVisible()) {
            this.bindTexture(playerHandler.getEarLocation());
            GL11.glPushMatrix();
            float f2 = 1.3333334F;
            GL11.glScalef(f2, f2, f2);
            if(playerEntity.isSneaking()) {
                GL11.glTranslatef(0.0F, 0.2F, 0.0F);
            }
            bipedModel.renderEars(0.0625F);
            GL11.glPopMatrix();
        }
    }

    /**
     * Render Cape
     * @param playerEntity
     * @param f
     * @param ci
     */
    @Inject(method = "renderMore(Lnet/minecraft/entity/player/PlayerEntity;F)V", at = @At(value = "TAIL"))
    public void minecraftcapes$renderCape(PlayerEntity playerEntity, float f, CallbackInfo ci) {
        if(playerHandler != null && playerHandler.getCapeLocation() >= 0 && MinecraftCapesConfig.isCapeVisible()) {
            this.bindTexture(playerHandler.getCapeLocation());
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double var20 = playerEntity.prevCapeX + (playerEntity.capeX - playerEntity.prevCapeX) * (double) f - (playerEntity.prevX + (playerEntity.x - playerEntity.prevX) * (double) f);
            double var26 = playerEntity.prevCapeY + (playerEntity.capeY - playerEntity.prevCapeY) * (double) f - (playerEntity.prevY + (playerEntity.y - playerEntity.prevY) * (double) f);
            double var8 = playerEntity.prevCapeZ + (playerEntity.capeZ - playerEntity.prevCapeZ) * (double) f - (playerEntity.prevZ + (playerEntity.z - playerEntity.prevZ) * (double) f);
            float var10 = playerEntity.lastBodyYaw + (playerEntity.bodyYaw - playerEntity.lastBodyYaw) * f;
            double var11 = (double) MathHelper.sin(var10 * (float) Math.PI / 180.0F);
            double var13 = (double) (-MathHelper.cos(var10 * (float) Math.PI / 180.0F));
            float var15 = (float) var26 * 10.0F;
            if (var15 < -6.0F) {
                var15 = -6.0F;
            }

            if (var15 > 32.0F) {
                var15 = 32.0F;
            }

            float var16 = (float) (var20 * var11 + var8 * var13) * 100.0F;
            float var17 = (float) (var20 * var13 - var8 * var11) * 100.0F;
            if (var16 < 0.0F) {
                var16 = 0.0F;
            }

            float var18 = playerEntity.prevStepBobbingAmount + (playerEntity.stepBobbingAmount - playerEntity.prevStepBobbingAmount) * f;
            var15 += MathHelper.sin((playerEntity.prevHorizontalSpeed + (playerEntity.horizontalSpeed - playerEntity.prevHorizontalSpeed) * f) * 6.0F) * 32.0F * var18;
            if (playerEntity.isSneaking()) {
                var15 += 25.0F;
            }

            GL11.glRotatef(6.0F + var16 / 2.0F + var15, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var17 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-var17 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            this.bipedModel.renderCape(0.0625F);
            GL11.glPopMatrix();
        }
    }

    /**
     * Bind a texture to active
     * @param id The texture id
     */
    @Unique
    private void bindTexture(int id) {
        Minecraft gameInstance = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();
        gameInstance.textureManager.bindTexture(id);
    }
}
