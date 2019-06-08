package co.uk.minecraftcapes.render;

import com.mojang.blaze3d.platform.GlStateManager;

import co.uk.minecraftcapes.events.PlayerEventHandler;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CapeLayer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {
   public CapeLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> p_i50950_1_) {
      super(p_i50950_1_);
   }

   public void func_212842_a_(AbstractClientPlayerEntity p_212842_1_, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_) {
      if (p_212842_1_.hasPlayerInfo() && !p_212842_1_.isInvisible() && p_212842_1_.isWearing(PlayerModelPart.CAPE) && p_212842_1_.getLocationCape() != null) {
         ItemStack itemstack = p_212842_1_.getItemStackFromSlot(EquipmentSlotType.CHEST);
         if (itemstack.getItem() != Items.ELYTRA) {
         	ResourceLocation rl = PlayerEventHandler.getCapeResourceLocation(p_212842_1_);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.func_215333_a(rl);
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0.0F, 0.0F, 0.125F);
            double d0 = MathHelper.func_219803_d((double)p_212842_4_, p_212842_1_.prevChasingPosX, p_212842_1_.chasingPosX) - MathHelper.func_219803_d((double)p_212842_4_, p_212842_1_.prevPosX, p_212842_1_.posX);
            double d1 = MathHelper.func_219803_d((double)p_212842_4_, p_212842_1_.prevChasingPosY, p_212842_1_.chasingPosY) - MathHelper.func_219803_d((double)p_212842_4_, p_212842_1_.prevPosY, p_212842_1_.posY);
            double d2 = MathHelper.func_219803_d((double)p_212842_4_, p_212842_1_.prevChasingPosZ, p_212842_1_.chasingPosZ) - MathHelper.func_219803_d((double)p_212842_4_, p_212842_1_.prevPosZ, p_212842_1_.posZ);
            float f = p_212842_1_.prevRenderYawOffset + (p_212842_1_.renderYawOffset - p_212842_1_.prevRenderYawOffset);
            double d3 = (double)MathHelper.sin(f * ((float)Math.PI / 180F));
            double d4 = (double)(-MathHelper.cos(f * ((float)Math.PI / 180F)));
            float f1 = (float)d1 * 10.0F;
            f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
            float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
            f2 = MathHelper.clamp(f2, 0.0F, 150.0F);
            float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;
            f3 = MathHelper.clamp(f3, -20.0F, 20.0F);
            if (f2 < 0.0F) {
               f2 = 0.0F;
            }

            float f4 = MathHelper.func_219799_g(p_212842_4_, p_212842_1_.prevCameraYaw, p_212842_1_.cameraYaw);
            f1 = f1 + MathHelper.sin(MathHelper.func_219799_g(p_212842_4_, p_212842_1_.prevDistanceWalkedModified, p_212842_1_.distanceWalkedModified) * 6.0F) * 32.0F * f4;
            if (p_212842_1_.func_213287_bg()) {
               f1 += 25.0F;
            }

            GlStateManager.rotatef(6.0F + f2 / 2.0F + f1, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotatef(f3 / 2.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotatef(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
            this.func_215332_c().renderCape(0.0625F);
            GlStateManager.popMatrix();
         }
      }
   }

   public boolean shouldCombineTextures() {
      return false;
   }
}