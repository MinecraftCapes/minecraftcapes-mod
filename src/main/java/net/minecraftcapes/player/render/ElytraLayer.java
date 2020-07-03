package net.minecraftcapes.player.render;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftcapes.player.PlayerHandler;

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
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		if (itemstack.getItem() == Items.ELYTRA) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			AbstractClientPlayer abstractclientplayerentity = (AbstractClientPlayer) entitylivingbaseIn;
			PlayerHandler playerHandler = PlayerHandler.getFromPlayer(abstractclientplayerentity);
			if (abstractclientplayerentity.hasPlayerInfo() && playerHandler.getCapeLocation() != null && abstractclientplayerentity.isWearing(EnumPlayerModelParts.CAPE)) {
				this.renderPlayer.bindTexture(playerHandler.getCapeLocation());
			} else {
				this.renderPlayer.bindTexture(TEXTURE_ELYTRA);
			}

			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, 0.0F, 0.125F);
			this.modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
			this.modelElytra.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			if (itemstack.isItemEnchanted()) {
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