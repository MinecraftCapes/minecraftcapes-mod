package net.minecraftcapes.player.downloader;

import net.minecraftcapes.helpers.Downloader;
import net.minecraftcapes.helpers.IImageBuffer;
import net.minecraftcapes.player.PlayerHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

import static net.minecraftcapes.MinecraftCapes.MODID;

public class DownloadCape {

	public static void download(String capeUrl, PlayerHandler playerHandler) {
		UUID playerUUID = playerHandler.getPlayerUUID();
	    if(playerUUID != null) {
		    ResourceLocation rl = new ResourceLocation(MODID, "capes/" + playerUUID);
		    TextureManager textureManager = Minecraft.getInstance().getTextureManager();
		    Downloader textureCape = new Downloader(capeUrl, playerHandler.getCapeLocation(), iImageBuffer, playerHandler);
            textureManager.loadTexture(rl, textureCape);
		}
	}

	private static final IImageBuffer iImageBuffer = new IImageBuffer() {
		@Override
		public NativeImage parseTexture(NativeImage img, PlayerHandler playerHandler) {
			int imageWidth = 64;
			int imageHeight = 32;

			for (int srcWidth = img.getWidth(), srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageWidth *= 2, imageHeight *= 2) {}

			final NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
			for (int x = 0; x < img.getWidth(); x++) {
				for (int y = 0; y < img.getHeight(); y++) {
					imgNew.setPixelRGBA(x, y, img.getPixelRGBA(x, y));
				}
			}

			playerHandler.setHasStaticCape(true);
			img.close();
			return imgNew;
		}
	};
}