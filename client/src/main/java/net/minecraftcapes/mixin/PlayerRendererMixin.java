package net.minecraftcapes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.client.render.model.Model;
import net.minecraft.client.render.model.entity.HumanoidModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.player.PlayerEntity;
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

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin extends MobRenderer<PlayerEntity> {

    @Shadow
    private HumanoidModel player;

    @Unique
    private PlayerHandler playerHandler;

    @Unique
    private boolean needsPlayerHandler = true;

    public PlayerRendererMixin(Model model, float shadowSize) {
        super(model, shadowSize);
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
    @Inject(method = "render(Lnet/minecraft/entity/mob/player/PlayerEntity;DDDFF)V", at = @At(value = "TAIL"))
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
     * Dont render double capes
     * @param instance PlayerRenderer instance
     * @param url URL to download
     * @param backup Backup URL
     * @return
     */
    @Redirect(method = "renderMore(Lnet/minecraft/entity/mob/player/PlayerEntity;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerRenderer;bindHttpTexture(Ljava/lang/String;Ljava/lang/String;)Z"))
    public boolean minecraftcapes$renderCape(PlayerRenderer instance, String url, String backup, @Local(argsOnly = true) PlayerEntity playerEntity) {
        if(playerEntity.cape.equals(url)) {
            if (MinecraftCapesConfig.isCapeVisible() && playerHandler != null && playerHandler.getCapeLocation() >= 0) {
                return false;
            }
        }
        return this.bindHttpTexture(url, backup);
    }

    /**
     * Render Ears
     * @param playerEntity The player entity
     * @param f Delta
     * @param ci Callback
     */
    @Inject(method = "renderMore(Lnet/minecraft/entity/mob/player/PlayerEntity;F)V", at = @At(value = "TAIL"))
    public void minecraftcapes$renderEars(PlayerEntity playerEntity, float f, CallbackInfo ci) {
        if(MinecraftCapesConfig.isEarsVisible() && playerHandler != null && playerHandler.getEarLocation() >= 0) {
            this.dispatcher.textureManager.bind(playerHandler.getEarLocation());
            GL11.glPushMatrix();
            float f2 = 1.3333334F;
            GL11.glScalef(f2, f2, f2);
            if(playerEntity.isSneaking()) {
                GL11.glTranslatef(0.0F, 0.1F, 0.0F);
            }
            player.renderDeadmau5Ears(0.0625F);
            GL11.glPopMatrix();
        }
    }

    /**
     * Render Cape
     * @param playerEntity The player entity
     * @param f Delta
     * @param ci Callback
     */
    @Inject(method = "renderMore(Lnet/minecraft/entity/mob/player/PlayerEntity;F)V", at = @At(value = "TAIL"))
    public void minecraftcapes$renderCape(PlayerEntity playerEntity, float f, CallbackInfo ci) {
        if(MinecraftCapesConfig.isCapeVisible() && playerHandler != null && playerHandler.getCapeLocation() >= 0) {
            this.dispatcher.textureManager.bind(playerHandler.getCapeLocation());
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double d = playerEntity.lastCapeX + (playerEntity.capeX - playerEntity.lastCapeX) * (double)f - (playerEntity.lastX + (playerEntity.x - playerEntity.lastX) * (double)f);
            double e = playerEntity.lastCapeY + (playerEntity.capeY - playerEntity.lastCapeY) * (double)f - (playerEntity.lastY + (playerEntity.y - playerEntity.lastY) * (double)f);
            double f15 = playerEntity.lastCapeZ + (playerEntity.capeZ - playerEntity.lastCapeZ) * (double)f - (playerEntity.lastZ + (playerEntity.z - playerEntity.lastZ) * (double)f);
            float m = playerEntity.lastBodyYaw + (playerEntity.bodyYaw - playerEntity.lastBodyYaw) * f;
            double g17 = MathHelper.sin(m * (float)Math.PI / 180.0F);
            double h18 = -MathHelper.cos(m * (float)Math.PI / 180.0F);
            float n = (float)e * 10.0F;
            if (n < -6.0F) {
                n = -6.0F;
            }

            if (n > 32.0F) {
                n = 32.0F;
            }

            float o = (float)(d * g17 + f15 * h18) * 100.0F;
            float p = (float)(d * h18 - f15 * g17) * 100.0F;
            if (o < 0.0F) {
                o = 0.0F;
            }

            float q = playerEntity.lastBob + (playerEntity.bob - playerEntity.lastBob) * f;
            n += MathHelper.sin((playerEntity.lastWalkDistance + (playerEntity.walkDistance - playerEntity.lastWalkDistance) * f) * 6.0F) * 32.0F * q;
            if (playerEntity.isSneaking()) {
                n += 25.0F;
            }

            GL11.glRotatef(6.0F + o / 2.0F + n, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(p / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-p / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            this.player.renderCape(0.0625F);

            if(playerHandler.getHasCapeGlint()) {
                this.minecraftcapes$renderEnchantmentGlint(playerEntity, f);
            }

            GL11.glPopMatrix();
        }
    }

    /**
     * Rotate the player
     * @param instance MobRenderer instance of the redirect
     * @param entity The entity to rotate
     * @param bob The entity bob
     * @param bodyYaw The entity body yaw
     * @param tickDelta The entity tick delta
     */
    @Redirect(method = "applyRotation(Lnet/minecraft/entity/mob/player/PlayerEntity;FFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/MobRenderer;applyRotation(Lnet/minecraft/entity/mob/MobEntity;FFF)V"))
    public void minecraftcapes$renderDinnerbone(MobRenderer instance, MobEntity entity, float bob, float bodyYaw, float tickDelta) {
        if(playerHandler != null && playerHandler.isUpsideDown()) {
            GL11.glTranslatef(0.0F, entity.height + 0.1F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(180.0F + bodyYaw, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F + bodyYaw, 0.0F, 1.0F, 0.0F);
        }
        super.applyRotation((PlayerEntity) entity, bob, bodyYaw, tickDelta);
    }

    /**
     * Render the enchantment for the cape if needed
     * @param playerEntity
     * @param partialTicks
     */
    @Unique
    private void minecraftcapes$renderEnchantmentGlint(PlayerEntity playerEntity, float partialTicks) {
        float f8 = (float) playerEntity.ticks + partialTicks;
        this.bindTexture("/assets/minecraftcapes/glint.png");
        GL11.glEnable(GL11.GL_BLEND);
        float f9 = 0.5F;
        GL11.glColor4f(f9, f9, f9, 1.0F);
        GL11.glDepthFunc(GL11.GL_EQUAL);
        GL11.glDepthMask(false);

        for (int k = 0; k < 2; ++k)
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            float f10 = 0.76F;
            GL11.glColor4f(0.5F * f10, 0.25F * f10, 0.8F * f10, 1.0F);
            GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glLoadIdentity();
            float f11 = f8 * (0.001F + (float)k * 0.003F) * 20.0F;
            float f12 = 0.33333334F;
            GL11.glScalef(f12, f12, f12);
            GL11.glRotatef(30.0F - (float)k * 60.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(0.0F, f11, 0.0F);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            this.player.renderCape(0.0625F);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glDepthMask(true);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
    }
}
