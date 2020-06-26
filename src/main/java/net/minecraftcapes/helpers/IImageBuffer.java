package net.minecraftcapes.helpers;

import net.minecraftcapes.player.PlayerHandler;

import java.awt.image.BufferedImage;

public interface IImageBuffer {

	BufferedImage parseTexture(BufferedImage img, PlayerHandler playerHandler);
	
}