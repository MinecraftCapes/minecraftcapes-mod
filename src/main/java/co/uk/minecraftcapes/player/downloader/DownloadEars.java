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

public class DownloadEars {

	private static UUID playerUUID;

	public static void download(final UUID uuid) {
		playerUUID = uuid;
		if(playerUUID != null) {
	    	String url = "https://minecraftcapes.co.uk/getEars/" + uuid.toString().replace("-", "");
	    	
	    	ResourceLocation rl = new ResourceLocation(MODID, "ears/" + uuid);
	    	TextureManager textureManager = Minecraft.getInstance().getTextureManager();
	    		    	
	    	Downloader textureEars = new Downloader(url, null, iImageBuffer);
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
			PlayerEventHandler.setEars(playerUUID);

			return imgNew;
		}

		@Override
		public void handleAnimatedCape(Int2ObjectMap<NativeImage> animatedCape) {}
	};
}