package net.minecraftcapes.player.downloader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftcapes.helpers.Downloader;
import net.minecraftcapes.helpers.IImageBuffer;
import net.minecraftcapes.player.PlayerHandler;

import java.util.UUID;

import static net.minecraftcapes.MinecraftCapes.MODID;

public class DownloadEars {

	private static UUID playerUUID;

	public static void download(String earsUrl, PlayerHandler playerHandler) {
		DownloadEars.playerUUID = playerHandler.getPlayerUUID();
		if(playerUUID != null) {
	    	ResourceLocation rl = new ResourceLocation(MODID, "ears/" + playerUUID);
	    	TextureManager textureManager = Minecraft.getInstance().getTextureManager();
	    	Downloader textureEars = new Downloader(earsUrl, playerHandler.getEarLocation(), iImageBuffer, playerHandler);
	    	textureManager.loadTexture(rl, textureEars);
		}
	}

	private static final IImageBuffer iImageBuffer = new IImageBuffer() {
		@Override
		public NativeImage parseTexture(NativeImage img, PlayerHandler playerHandler) {
			NativeImage imgNew = new NativeImage(64, 64, true);
			for(int imgHeight = 0; imgHeight < img.getHeight(); imgHeight++) {
				for(int imgWidth = 0; imgWidth < img.getWidth(); imgWidth++) {
					imgNew.setPixelRGBA(24 + imgWidth, imgHeight, img.getPixelRGBA(imgWidth, imgHeight));
				}
			}
			img.close();
			playerHandler.setHasEars(true);
			return imgNew;
		}
	};
}