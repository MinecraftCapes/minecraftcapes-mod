package net.minecraftcapes.player.downloader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftcapes.helpers.Downloader;
import net.minecraftcapes.helpers.IImageBuffer;
import net.minecraftcapes.player.PlayerHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static net.minecraftcapes.MinecraftCapes.MODID;

public class DownloadEars {

	public static void download(String earsUrl, PlayerHandler playerHandler) {
		UUID playerUUID = playerHandler.getPlayerUUID();
		if(playerUUID != null) {
	    	ResourceLocation rl = new ResourceLocation(MODID, "ears/" + playerUUID);
	    	TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
	    	Downloader textureEars = new Downloader(earsUrl, playerHandler.getEarLocation(), iImageBuffer, playerHandler);
	    	textureManager.loadTexture(rl, textureEars);
		}
	}

	private static final IImageBuffer iImageBuffer = new IImageBuffer() {
		@Override
		public BufferedImage parseTexture(BufferedImage img, PlayerHandler playerHandler) {
			BufferedImage imgNew = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
			Graphics g = imgNew.getGraphics();
			g.drawImage(img, 24, 0, null);
			g.dispose();
			playerHandler.setHasEars(true);

			return imgNew;
		}
	};
}