package co.uk.minecraftcapes.player.downloader;

import static co.uk.minecraftcapes.reference.Reference.MODID;

import co.uk.minecraftcapes.events.PlayerEventHandler;
import co.uk.minecraftcapes.helpers.Downloader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class DownloadEars {
 
	public static void download(final String uuid) {
		
	    if ((uuid != null) && (!uuid.isEmpty())) {
	    	    	
	    	String url = "https://MinecraftCapes.co.uk/getEars/" + uuid;
	    	
	    	ResourceLocation rl = new ResourceLocation(MODID, "ears/" + uuid);
	    	TextureManager textureManager = Minecraft.getInstance().getTextureManager();
	    			           			      		   		     
	    	IImageBuffer iib = new IImageBuffer() {	    				    		
	    		public NativeImage parseUserSkin(NativeImage img) {
	    			return parseEars(img, uuid);
	    		}
				
				public void skinAvailable() {}
	    	};
	    	
	    	Downloader textureEars = new Downloader(url, null, iib);
	    	textureManager.loadTexture(rl, textureEars);			    	
		}
	}
	
	public static NativeImage parseEars(NativeImage img, String uuid) {		
	    NativeImage imgNew = new NativeImage(64, 64, true);
	    
	    for(int imgHeight = 0; imgHeight < img.getHeight(); imgHeight++) {
	    	for(int imgWidth = 0; imgWidth < img.getWidth(); imgWidth++) {	    		
	    		imgNew.setPixelRGBA(24 + imgWidth, imgHeight, img.getPixelRGBA(imgWidth, imgHeight));
	    	}
	    }			  
	    img.close();
	    PlayerEventHandler.playersEar.put(uuid, true);
	    
	    return imgNew;
    }
}