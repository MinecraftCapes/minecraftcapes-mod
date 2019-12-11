package co.uk.minecraftcapes.mixin;

import java.util.ListIterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import co.uk.minecraftcapes.render.CapeLayer;
import co.uk.minecraftcapes.render.Deadmau5;
import co.uk.minecraftcapes.render.ElytraLayer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

@Mixin(PlayerEntityRenderer.class)
public abstract class MixinPlayerEntityRenderer extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	public MixinPlayerEntityRenderer(EntityRenderDispatcher entityRenderDispatcher_1, PlayerEntityModel<AbstractClientPlayerEntity> entityModel_1, float float_1) {
		super(entityRenderDispatcher_1, entityModel_1, float_1); 
	}

	@Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;Z)V", at = @At("RETURN"))
	private void construct(EntityRenderDispatcher entityRenderDispatcher, boolean alex, CallbackInfo info){
		addFeature(new CapeLayer.LayerRender(this));
		addFeature(new Deadmau5.LayerRender(this));
		addFeature(new ElytraLayer.LayerRender(this));
		
		ListIterator<FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>> it = features.listIterator();
		while(it.hasNext()) {					
			if(it.next() instanceof net.minecraft.client.render.entity.feature.ElytraFeatureRenderer) {
				it.remove();
			}
		}
	}	

}
