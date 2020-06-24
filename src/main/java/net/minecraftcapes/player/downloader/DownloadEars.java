package net.minecraftcapes.player.downloader;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import net.minecraftcapes.helpers.Downloader;
import net.minecraftcapes.helpers.ImageFilter;
import net.minecraftcapes.player.PlayerHandler;

import java.util.UUID;

import static net.minecraftcapes.MinecraftCapes.MODID;

public class DownloadEars {

	public static void download(String earsUrl, PlayerHandler playerHandler) {
		UUID playerUUID = playerHandler.getPlayerUUID();
		if(playerUUID != null) {
			Identifier rl = new Identifier(MODID, "ears/" + playerUUID);
			TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
			Downloader textureEars = new Downloader(earsUrl, playerHandler.getEarLocation(), imageFilter, playerHandler);
			textureManager.registerTexture(rl, textureEars);
		}
	}

	private static final ImageFilter imageFilter = (img, playerHandler) -> {
		NativeImage imgNew = new NativeImage(64, 64, true);
		for(int imgHeight = 0; imgHeight < img.getHeight(); imgHeight++) {
			for(int imgWidth = 0; imgWidth < img.getWidth(); imgWidth++) {
				imgNew.setPixelRgba(24 + imgWidth, imgHeight, img.getPixelRgba(imgWidth, imgHeight));
			}
		}
		img.close();
		playerHandler.setHasEars(true);
		return imgNew;
	};
}