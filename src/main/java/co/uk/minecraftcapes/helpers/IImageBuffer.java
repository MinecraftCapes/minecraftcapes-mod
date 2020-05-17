package co.uk.minecraftcapes.helpers;

import co.uk.minecraftcapes.capabilities.PlayerHandler;
import net.minecraft.client.renderer.texture.NativeImage;

public interface IImageBuffer {

	NativeImage parseTexture(NativeImage img, PlayerHandler playerHandler);
	
}