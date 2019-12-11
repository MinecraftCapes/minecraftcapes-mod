package co.uk.minecraftcapes.player.downloader;

import static co.uk.minecraftcapes.reference.Reference.MODID;

import co.uk.minecraftcapes.events.PlayerEventHandler;
import co.uk.minecraftcapes.helpers.Downloader;
import co.uk.minecraftcapes.helpers.ImageFilter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

public class DownloadEars {
 
	public static void download(final String uuid) {
		
	    if ((uuid != null) && (!uuid.isEmpty())) {
	    	    	
	    	String url = "https://MinecraftCapes.co.uk/getEars/" + uuid;	    	
	    	
	    	Identifier rl = new Identifier(MODID, "ears/" + uuid);
	    	TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
	    			           			      		   		     
	    	ImageFilter imgFilter = new ImageFilter() {	    				    		
				@Override
				public NativeImage filterImage(NativeImage img) {
	    			return parseEars(img, uuid);
	    		}
	    	};
	    		    	
	    	Downloader textureEars = new Downloader(url, null, imgFilter);
	    	textureManager.registerTexture(rl, textureEars);			    	
		}
	}
	
	public static NativeImage parseEars(NativeImage img, String uuid) {
	    NativeImage imgNew = new NativeImage(64, 64, true);
	    
	    for(int imgHeight = 0; imgHeight < img.getHeight(); imgHeight++) {
	    	for(int imgWidth = 0; imgWidth < img.getWidth(); imgWidth++) {	    		
	    		imgNew.setPixelRgba(24 + imgWidth, imgHeight, img.getPixelRgba(imgWidth, imgHeight));
	    	}
	    }			  
	    img.close();
	    PlayerEventHandler.playersEar.put(uuid, true);
	    
	    return imgNew;
    }
}