package net.minecraftcapes.player.render;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.ModelElytra;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftcapes.player.PlayerHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ElytraLayer implements LayerRenderer<EntityLivingBase> {
	/** The basic Elytra texture. */
	private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");
	/** Instance of the player renderer. */
	protected final RenderLivingBase<?> renderPlayer;
	/** The model used by the Elytra. */
	private final ModelElytra modelElytra = new ModelElytra();

	public ElytraLayer(RenderLivingBase<?> p_i47185_1_) {
		this.renderPlayer = p_i47185_1_;
	}

	@Override
	public void render(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		if (itemstack.getItem() == Items.ELYTRA) {
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			AbstractClientPlayer abstractclientplayerentity = (AbstractClientPlayer) entitylivingbaseIn;
			PlayerHandler playerHandler = PlayerHandler.getFromPlayer(abstractclientplayerentity);
			ResourceLocation capeLocation = playerHandler.getCapeLocation();
			if (abstractclientplayerentity.hasPlayerInfo() && capeLocation != null && abstractclientplayerentity.isWearing(EnumPlayerModelParts.CAPE)) {
				this.renderPlayer.bindTexture(capeLocation);
			} else {
				this.renderPlayer.bindTexture(TEXTURE_ELYTRA);
			}

			GlStateManager.pushMatrix();
			GlStateManager.translatef(0.0F, 0.0F, 0.125F);
			this.modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
			this.modelElytra.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			if (itemstack.isEnchanted()) {
				LayerArmorBase.renderEnchantedGlint(this.renderPlayer, entitylivingbaseIn, this.modelElytra, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
			}

			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}