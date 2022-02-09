package net.minecraftcapes.mixin;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.client.render.entity.model.Biped;
import net.minecraft.client.render.entity.model.EntityModelBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.maths.MathHelper;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.helpers.GetMinecraftInstance;
import net.minecraftcapes.player.PlayerHandler;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerRenderer extends LivingEntityRenderer {

    @Shadow
    private Biped field_294;

    public MixinPlayerRenderer(EntityModelBase player, float particleTicks) {
        super(player, particleTicks);
    }

    @Inject(method = "method_827(Lnet/minecraft/entity/player/PlayerBase;F)V", at = @At("HEAD"))
    private void onCapeRender(PlayerBase player, float particleTicks, CallbackInfo ci) {
        if(MinecraftCapesConfig.isCapeVisible()) {
            PlayerHandler playerHandler = PlayerHandler.getFromPlayer(player);
            if(playerHandler.getCape() != null) {
                playerHandler.bindCape();
                GL11.glPushMatrix();
                GL11.glTranslatef(0.0F, 0.0F, 0.125F);
                double var20 = player.field_530 + (player.field_533 - player.field_530) * (double) particleTicks - (player.prevX + (player.x - player.prevX) * (double) particleTicks);
                double var22 = player.field_531 + (player.field_534 - player.field_531) * (double) particleTicks - (player.prevY + (player.y - player.prevY) * (double) particleTicks);
                double var8 = player.field_532 + (player.field_535 - player.field_532) * (double) particleTicks - (player.prevZ + (player.z - player.prevZ) * (double) particleTicks);
                float var10 = player.field_1013 + (player.field_1012 - player.field_1013) * particleTicks;
                double var11 = MathHelper.sin(var10 * 3.1415927F / 180.0F);
                double var13 = -MathHelper.cos(var10 * 3.1415927F / 180.0F);
                float var15 = (float) var22 * 10.0F;
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

                float var18 = player.field_524 + (player.field_525 - player.field_524) * particleTicks;
                var15 += MathHelper.sin((player.field_1634 + (player.field_1635 - player.field_1634) * particleTicks) * 6.0F) * 32.0F * var18;
                if (player.method_1373()) {
                    var15 += 25.0F;
                }

                GL11.glRotatef(6.0F + var16 / 2.0F + var15, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(var17 / 2.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-var17 / 2.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                this.field_294.method_606(0.0625F);
                GL11.glPopMatrix();
            }
        }
    }

    @Inject(method = "method_827(Lnet/minecraft/entity/player/PlayerBase;F)V", at = @At("HEAD"))
    private void onEarsRender(PlayerBase player, float particleTicks, CallbackInfo ci) {
        if(MinecraftCapesConfig.isEarsVisible()) {
            PlayerHandler playerHandler = PlayerHandler.getFromPlayer(player);
            if(playerHandler.getEars() != null) {
                float headRotation;
                playerHandler.bindEars();
                for (int earIteration = 0; earIteration < 2; ++earIteration) {
                    headRotation = player.prevYaw + (player.yaw - player.prevYaw) * particleTicks - (player.field_1013 + (player.field_1012 - player.field_1013) * particleTicks);
                    float headPitch = player.prevPitch + (player.pitch - player.prevPitch) * particleTicks;
                    GL11.glPushMatrix();
                    GL11.glRotatef(headRotation, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(headPitch, 1.0F, 0.0F, 0.0F);
                    GL11.glTranslatef(0.375F * (float) (earIteration * 2 - 1), 0.0F, 0.0F);

                    float yPos = -0.325F;
                    if(player.method_1373()) {
                        yPos = -0.25F;
                    }
                    GL11.glTranslatef(0.0F, yPos, 0.0F);


                    GL11.glRotatef(-headPitch, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(-headRotation, 0.0F, 1.0F, 0.0F);
                    float glScale = 1.3333334F;
                    GL11.glScalef(glScale, glScale, glScale);
                    this.field_294.method_605(0.0625F);
                    GL11.glPopMatrix();
                }
            }
        }
    }
}
