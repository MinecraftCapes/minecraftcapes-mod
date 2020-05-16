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

public class DownloadEars {

	private static UUID playerUUID;

	public static void download(String earsUrl, UUID playerUUID) {
		DownloadEars.playerUUID = playerUUID;
		if(playerUUID != null) {
	    	ResourceLocation rl = new ResourceLocation(MODID, "ears/" + playerUUID);
	    	TextureManager textureManager = Minecraft.getInstance().getTextureManager();
	    	Downloader textureEars = new Downloader(earsUrl, null, iImageBuffer);
	    	textureManager.loadTexture(rl, textureEars);
		}
	}

	private static final IImageBuffer iImageBuffer = new IImageBuffer() {
		@Override
		public NativeImage parseTexture(NativeImage img) {
			NativeImage imgNew = new NativeImage(64, 64, true);
			for(int imgHeight = 0; imgHeight < img.getHeight(); imgHeight++) {
				for(int imgWidth = 0; imgWidth < img.getWidth(); imgWidth++) {
					imgNew.setPixelRGBA(24 + imgWidth, imgHeight, img.getPixelRGBA(imgWidth, imgHeight));
				}
			}
			img.close();
			PlayerHandler.getPlayer(playerUUID).setHasEars(true);
			return imgNew;
		}

		@Override
		public void handleAnimatedCape(Int2ObjectMap<NativeImage> animatedCape) {}
	};
}