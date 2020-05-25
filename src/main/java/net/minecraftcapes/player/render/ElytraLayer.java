package net.minecraftcapes.player.render;

import net.minecraftcapes.player.PlayerHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.ElytraModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
	private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");
	private final ElytraModel<T> modelElytra = new ElytraModel<>();

	public ElytraLayer(IEntityRenderer<T, M> rendererIn) {
		super(rendererIn);
	}

	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EquipmentSlotType.CHEST);
		if (itemstack.getItem() == Items.ELYTRA) {
			AbstractClientPlayerEntity abstractclientplayerentity = (AbstractClientPlayerEntity)entitylivingbaseIn;
			PlayerHandler playerHandler = PlayerHandler.getFromPlayer(abstractclientplayerentity);
			ResourceLocation capeLocation = playerHandler.getCapeLocation();
			ResourceLocation resourcelocation;
			if (abstractclientplayerentity.hasPlayerInfo() && capeLocation != null && abstractclientplayerentity.isWearing(PlayerModelPart.CAPE)) {
				resourcelocation = capeLocation;
			} else {
				resourcelocation = TEXTURE_ELYTRA;
			}

			matrixStackIn.push();
			matrixStackIn.translate(0.0D, 0.0D, 0.125D);
			this.getEntityModel().copyModelAttributesTo(this.modelElytra);
			this.modelElytra.setRotationAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			IVertexBuilder ivertexbuilder = ItemRenderer.getBuffer(bufferIn, this.modelElytra.getRenderType(resourcelocation), false, itemstack.hasEffect());
			this.modelElytra.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			matrixStackIn.pop();
		}
	}
}