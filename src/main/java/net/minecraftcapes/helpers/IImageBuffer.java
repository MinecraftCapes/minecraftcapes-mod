package net.minecraftcapes.helpers;

import net.minecraftcapes.player.PlayerHandler;
import net.minecraft.client.renderer.texture.NativeImage;

public interface IImageBuffer {

	NativeImage parseTexture(NativeImage img, PlayerHandler playerHandler);
	
}