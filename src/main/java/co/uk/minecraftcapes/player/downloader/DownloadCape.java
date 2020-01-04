package co.uk.minecraftcapes.player.downloader;

import static co.uk.minecraftcapes.reference.Reference.MODID;

import co.uk.minecraftcapes.events.PlayerEventHandler;
import co.uk.minecraftcapes.helpers.Downloader;
import co.uk.minecraftcapes.helpers.IImageBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class DownloadCape {
	
	public static void download(final String uuid) {
		
	    if ((uuid != null) && (!uuid.isEmpty())) {

	    	//String url = "https://minecraftcapes.co.uk/getCape/" + uuid;
			String url = "https://minecraftcapes.co.uk/getCape/061b485d1efa4b2fa2429cc59ce6370f";
	    	
		    ResourceLocation rl = new ResourceLocation(MODID, "capes/" + uuid);
		    TextureManager textureManager = Minecraft.getInstance().getTextureManager();
		                
		    IImageBuffer iib = new IImageBuffer() {	    		
				public NativeImage parseTexture(NativeImage img) {
	    			return parseCape(img, uuid);
				}
		    };		    		   
		                		    		  		    
		    Downloader textureCape = new Downloader(url, null, iib);
            textureManager.func_229263_a_(rl, textureCape);
		}
	}
	
	public static NativeImage parseCape(NativeImage img, String uuid) {		
        int imageWidth = 64;
        int imageHeight = 32;
        
        for (int srcWidth = img.getWidth(), srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageWidth *= 2, imageHeight *= 2) {}
        
        final NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
        
	    for(int x = 0; x < img.getWidth(); x++) {
	    	for(int y = 0; y < img.getHeight(); y++) {	    		
	    		imgNew.setPixelRGBA(x, y, img.getPixelRGBA(x, y));
	    	}
	    }
	    
        img.close();        
        PlayerEventHandler.playersCape.put(uuid, true);
        
        return imgNew;
	}
}