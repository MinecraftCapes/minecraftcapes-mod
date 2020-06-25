package net.minecraftcapes.helpers;

import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraftcapes.player.PlayerHandler;

public interface IImageBuffer {

	NativeImage parseTexture(NativeImage img, PlayerHandler playerHandler);
	
}