package net.minecraftcapes.helpers;

import net.minecraft.client.texture.NativeImage;
import net.minecraftcapes.player.PlayerHandler;

public interface ImageFilter {

	NativeImage filterImage(NativeImage img, PlayerHandler playerHandler);
	
}
