package co.uk.minecraftcapes.player.downloader;

import co.uk.minecraftcapes.player.PlayerInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class DownloadEars {
 
	public static void download(final String uuid) {
		
	    if ((uuid != null) && (!uuid.isEmpty())) {
	    	    	
	    	String url = "https://www.minecraftcapes.co.uk/getEars.php?uuid=" + uuid;
	    	
	    	ResourceLocation rl = new ResourceLocation("ears/" + uuid);
	    	TextureManager textureManager = Minecraft.getInstance().getTextureManager();
	    			           			      		   		     
	    	IImageBuffer iib = new IImageBuffer() {	    				    		
	    		public NativeImage parseUserSkin(NativeImage img) {
	    			return parseEars(img, uuid);
	    		}
				
				public void skinAvailable() {}
	    	};
	    	
	    	ImageDownloader textureEars = new ImageDownloader(url, null, iib);		    	
	    	textureManager.loadTexture(rl, textureEars);			    	
		}
	}
	
	public static NativeImage parseEars(NativeImage img, String uuid) {
		int imageWidth = 64;
	    int imageHeight = 64;
	    
	    for (int srcWidth = img.getWidth(), srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageWidth *= 2, imageHeight *= 2) {}
		
	    final NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
	    	    
	    imgNew.copyImageData(img);
        img.close();
	    
	    PlayerInfo.playersEar.put(uuid, true);
	    
	    return imgNew;
    }
}