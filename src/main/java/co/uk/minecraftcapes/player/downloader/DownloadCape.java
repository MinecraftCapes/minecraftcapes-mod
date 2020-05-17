package co.uk.minecraftcapes.player.downloader;

import co.uk.minecraftcapes.capabilities.PlayerHandler;
import co.uk.minecraftcapes.helpers.Downloader;
import co.uk.minecraftcapes.helpers.IImageBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

import static co.uk.minecraftcapes.reference.Reference.MODID;

public class DownloadCape {

	public static void download(String capeUrl, PlayerHandler playerHandler) {
		UUID playerUUID = playerHandler.getPlayerUUID();
	    if(playerUUID != null) {
		    ResourceLocation rl = new ResourceLocation(MODID, "capes/" + playerUUID);
		    TextureManager textureManager = Minecraft.getInstance().getTextureManager();
		    Downloader textureCape = new Downloader(capeUrl, null, iImageBuffer, playerHandler);
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