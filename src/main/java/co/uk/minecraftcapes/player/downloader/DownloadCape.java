package co.uk.minecraftcapes.player.downloader;

import co.uk.minecraftcapes.events.PlayerEventHandler;
import co.uk.minecraftcapes.helpers.Downloader;
import co.uk.minecraftcapes.helpers.IImageBuffer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

import static co.uk.minecraftcapes.reference.Reference.MODID;

public class DownloadCape {

	private static UUID playerUUID;

	public static void download(final UUID uuid) {
		playerUUID = uuid;
	    if(playerUUID != null) {
	    	//String url = "https://minecraftcapes.co.uk/getCape/" + playerUUID.toString().replace("-", "");
			String url = "https://i.imgur.com/FPNyJO3.gif";
	    	
		    ResourceLocation rl = new ResourceLocation(MODID, "capes/" + playerUUID);
		    TextureManager textureManager = Minecraft.getInstance().getTextureManager();
		                		    		  		    
		    Downloader textureCape = new Downloader(url, null, iImageBuffer);
            textureManager.loadTexture(rl, textureCape);
		}
	}

	private static final IImageBuffer iImageBuffer = new IImageBuffer() {
		@Override
		public void handleAnimatedCape(Int2ObjectMap<NativeImage> animatedCape) {
			PlayerEventHandler.setAnimatedCape(playerUUID, animatedCape);
		}

		@Override
		public NativeImage parseTexture(NativeImage img) {
			int imageWidth = 64;
			int imageHeight = 32;

			for (int srcWidth = img.getWidth(), srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageWidth *= 2, imageHeight *= 2) {
			}

			final NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
			for (int x = 0; x < img.getWidth(); x++) {
				for (int y = 0; y < img.getHeight(); y++) {
					imgNew.setPixelRGBA(x, y, img.getPixelRGBA(x, y));
				}
			}

			img.close();
			PlayerEventHandler.setCape(playerUUID);

			return imgNew;
		}
	};
}