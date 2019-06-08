package co.uk.minecraftcapes.render;

import static co.uk.minecraftcapes.reference.Reference.MODID;

import com.mojang.blaze3d.platform.GlStateManager;

import co.uk.minecraftcapes.events.PlayerEventHandler;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ArmorLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.ElytraModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
   /** The basic Elytra texture. */
   private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation(MODID, "elytra.png");
   /** The model used by the Elytra. */
   private final ElytraModel<T> modelElytra = new ElytraModel<>();

   public ElytraLayer(IEntityRenderer<T, M> p_i50942_1_) {
      super(p_i50942_1_);
   }

   public void func_212842_a_(T p_212842_1_, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_) {
      ItemStack itemstack = p_212842_1_.getItemStackFromSlot(EquipmentSlotType.CHEST);      
      if (itemstack.getItem() == Items.ELYTRA) {
         GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
         if (p_212842_1_ instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity abstractclientplayerentity = (AbstractClientPlayerEntity)p_212842_1_;
            ResourceLocation rl = PlayerEventHandler.getCapeResourceLocation(abstractclientplayerentity);
            if (rl != null) {
               this.func_215333_a(rl);            
            } else {
               this.func_215333_a(TEXTURE_ELYTRA);
            }
         } else {
            this.func_215333_a(TEXTURE_ELYTRA);
         }

         GlStateManager.pushMatrix();
         GlStateManager.translatef(0.0F, 0.0F, 0.125F);
         this.modelElytra.func_212844_a_(p_212842_1_, p_212842_2_, p_212842_3_, p_212842_5_, p_212842_6_, p_212842_7_, p_212842_8_);
         this.modelElytra.render(p_212842_1_, p_212842_2_, p_212842_3_, p_212842_5_, p_212842_6_, p_212842_7_, p_212842_8_);
         if (itemstack.isEnchanted()) {
            ArmorLayer.func_215338_a(this::func_215333_a, p_212842_1_, this.modelElytra, p_212842_2_, p_212842_3_, p_212842_4_, p_212842_5_, p_212842_6_, p_212842_7_, p_212842_8_);
         }

         GlStateManager.disableBlend();
         GlStateManager.popMatrix();
      }
   }

   public boolean shouldCombineTextures() {
      return false;
   }
}