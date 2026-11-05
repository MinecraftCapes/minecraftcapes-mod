package net.minecraftcapes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftcapes.config.MinecraftCapesConfig;
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

@Mixin(PlayerEntityRenderer.class)
public class PlayerRendererMixin extends LivingEntityRenderer {

    @Shadow
    private BipedEntityModel bipedModel;

    @Unique
    private PlayerHandler playerHandler;

    public PlayerRendererMixin(EntityModel entityModel, float shadowRadius) {
        super(entityModel, shadowRadius);
    }

    /**
     * Gets the first playerhandler instance
     */
    @Inject(method = "render(Lnet/minecraft/entity/player/PlayerEntity;DDDFF)V", at = @At(value = "TAIL"))
    public void minecraftcapes$getPlayerHandler(PlayerEntity entity, double dx, double dy, double dz, float yaw, float tickDelta, CallbackInfo ci) {
        playerHandler = PlayerHandler.get(entity.name);
        DownloadManager.prepareDownload(entity.name, false);
    }

    /**
     * Dont render double capes
     * @param instance PlayerRenderer instance
     * @param url URL to download
     * @param backup Backup URL
     * @return
     */
    @Redirect(method = "renderMore(Lnet/minecraft/entity/player/PlayerEntity;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;bindDownloadedTexture(Ljava/lang/String;Ljava/lang/String;)Z"))
    public boolean minecraftcapes$renderCape(PlayerEntityRenderer instance, String url, String backup, @Local(argsOnly = true) PlayerEntity entity) {
        if(entity.capeUrl.equals(url)) {
            if (MinecraftCapesConfig.isCapeVisible() && playerHandler != null && playerHandler.getCapeLocation() >= 0) {
                return false;
            }
        }
        return this.bindDownloadedTexture(url, backup);
    }

    /**
     * Render Ears
     * @param entity The player entity
     * @param tickDelta Delta
     * @param ci Callback
     */
    @Inject(method = "renderMore(Lnet/minecraft/entity/player/PlayerEntity;F)V", at = @At(value = "TAIL"))
    public void minecraftcapes$renderEars(PlayerEntity entity, float tickDelta, CallbackInfo ci) {
        if(MinecraftCapesConfig.isEarsVisible() && playerHandler != null && playerHandler.getEarLocation() >= 0) {
            this.dispatcher.textureManager.bindTexture(playerHandler.getEarLocation());
            GL11.glPushMatrix();
            float f2 = 1.3333334F;
            GL11.glScalef(f2, f2, f2);
            if(entity.isSneaking()) {
                GL11.glTranslatef(0.0F, 0.1F, 0.0F);
            }
            bipedModel.renderEars(0.0625F);
            GL11.glPopMatrix();
        }
    }

    /**
     * Render Cape
     * @param entity The player entity
     * @param tickDelta Delta
     * @param ci Callback
     */
    @Inject(method = "renderMore(Lnet/minecraft/entity/player/PlayerEntity;F)V", at = @At(value = "TAIL"))
    public void minecraftcapes$renderCape(PlayerEntity entity, float tickDelta, CallbackInfo ci) {
        if(MinecraftCapesConfig.isCapeVisible() && playerHandler != null && playerHandler.getCapeLocation() >= 0) {
            this.dispatcher.textureManager.bindTexture(playerHandler.getCapeLocation());
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double d = entity.prevCapeX + (entity.capeX - entity.prevCapeX) * (double)tickDelta - (entity.lastTickX + (entity.x - entity.lastTickX) * (double)tickDelta);
            double e = entity.prevCapeY + (entity.capeY - entity.prevCapeY) * (double)tickDelta - (entity.lastTickY + (entity.y - entity.lastTickY) * (double)tickDelta);
            double f15 = entity.prevCapeZ + (entity.capeZ - entity.prevCapeZ) * (double)tickDelta - (entity.lastTickZ + (entity.z - entity.lastTickZ) * (double)tickDelta);
            float m = entity.lastBodyYaw + (entity.bodyYaw - entity.lastBodyYaw) * tickDelta;
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

            float var18 = entity.prevStepBobbingAmount + (entity.stepBobbingAmount - entity.prevStepBobbingAmount) * tickDelta;
            n += MathHelper.sin((entity.prevHorizontalSpeed + (entity.horizontalSpeed - entity.prevHorizontalSpeed) * tickDelta) * 6.0F) * 32.0F * var18;
            if (entity.isSneaking()) {
                n += 25.0F;
            }

            GL11.glRotatef(6.0F + o / 2.0F + n, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(p / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-p / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            this.bipedModel.renderCape(0.0625F);

            if(playerHandler.getHasCapeGlint()) {
                this.minecraftcapes$renderEnchantmentGlint(entity, tickDelta);
            }

            GL11.glPopMatrix();
        }
    }

    /**
     * Rotate the player
     * @param instance MobRenderer instance of the redirect
     * @param entity The entity to rotate
     * @param headBob The entity bob
     * @param bodyYaw The entity body yaw
     * @param tickDelta The entity tick delta
     */
    @Redirect(method = "applyTranslation(Lnet/minecraft/entity/player/PlayerEntity;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;applyTranslation(Lnet/minecraft/entity/LivingEntity;DDD)V"))
    public void minecraftcapes$renderDinnerbone(LivingEntityRenderer instance, LivingEntity entity, double headBob, double bodyYaw, double tickDelta) {
        if(playerHandler != null && playerHandler.isUpsideDown()) {
            GL11.glTranslatef(0.0F, entity.height + 0.1F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef((float) (180.0F + bodyYaw), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef((float) (180.0F + bodyYaw), 0.0F, 1.0F, 0.0F);
        }
        super.applyTranslation(entity, headBob, bodyYaw, tickDelta);
    }

    /**
     * Render the enchantment for the cape if needed
     */
    @Unique
    private void minecraftcapes$renderEnchantmentGlint(PlayerEntity entity, float partialTicks) {
        float f8 = (float) entity.age + partialTicks;
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
            this.bipedModel.renderCape(0.0625F);
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
