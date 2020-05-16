package co.uk.minecraftcapes.player.downloader;

import co.uk.minecraftcapes.helpers.Downloader;
import co.uk.minecraftcapes.helpers.IImageBuffer;
import co.uk.minecraftcapes.helpers.PlayerHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

import static co.uk.minecraftcapes.reference.Reference.MODID;

public class DownloadCape {

	private static UUID playerUUID;

	public static void download(String capeUrl, UUID playerUUID) {
		DownloadCape.playerUUID = playerUUID;
	    if(playerUUID != null) {
		    ResourceLocation rl = new ResourceLocation(MODID, "capes/" + playerUUID);
		    TextureManager textureManager = Minecraft.getInstance().getTextureManager();
		    Downloader textureCape = new Downloader(capeUrl, null, iImageBuffer);
            textureManager.loadTexture(rl, textureCape);
		}
	}

	private static final IImageBuffer iImageBuffer = new IImageBuffer() {
		@Override
		public void handleAnimatedCape(Int2ObjectMap<NativeImage> animatedCape) {
			PlayerHandler.getPlayer(playerUUID).setAnimatedCape(animatedCape);
		}

		@Override
		public NativeImage parseTexture(NativeImage img) {
			int imageWidth = 64;
			int imageHeight = 32;

			for (int srcWidth = img.getWidth(), srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageWidth *= 2, imageHeight *= 2) {}

			final NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
			for (int x = 0; x < img.getWidth(); x++) {
				for (int y = 0; y < img.getHeight(); y++) {
					imgNew.setPixelRGBA(x, y, img.getPixelRGBA(x, y));
				}
			}

			img.close();
			PlayerHandler.getPlayer(playerUUID).setHasStaticCape(true);
			return imgNew;
		}
	};
}