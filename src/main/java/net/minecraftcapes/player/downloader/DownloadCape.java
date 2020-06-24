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

public class DownloadCape {

	public static void download(String capeUrl, PlayerHandler playerHandler) {
		UUID playerUUID = playerHandler.getPlayerUUID();
		if(playerUUID != null) {
			Identifier rl = new Identifier(MODID, "capes/" + playerUUID);
			TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
			Downloader textureCape = new Downloader(capeUrl, playerHandler.getCapeLocation(), imageBuffer, playerHandler);
			textureManager.registerTexture(rl, textureCape);
		}
	}

	private static final ImageFilter imageBuffer = (img, playerHandler) -> {
		int imageWidth = 64;
		int imageHeight = 32;

		for (int srcWidth = img.getWidth(), srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageWidth *= 2, imageHeight *= 2) {}

		final NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				imgNew.setPixelColor(x, y, img.getPixelColor(x, y));
			}
		}

		playerHandler.setHasStaticCape(true);
		img.close();
		return imgNew;
	};
}