package co.uk.minecraftcapes.helpers;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.renderer.texture.NativeImage;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public interface IImageBuffer {

	NativeImage parseTexture(NativeImage img);
	void handleAnimatedCape(Int2ObjectMap<NativeImage> animatedCape);
	
}