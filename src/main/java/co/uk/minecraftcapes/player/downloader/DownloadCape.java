package co.uk.minecraftcapes.player.downloader;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import co.uk.minecraftcapes.player.PlayerInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

public class DownloadCape {
 
	public static void download(final String uuid) {
		
	    if ((uuid != null) && (!uuid.isEmpty())) {
	    	    	
	    	String url = "https://minecraftcapes.co.uk/getCape/" + uuid;	    		    	
		    ResourceLocation rl = new ResourceLocation("capes/" + uuid);
		    TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();		   
		    
		    if(!resourceExists(rl)) {		    				    		    	
		    	IImageBuffer iib = new IImageBuffer() {
			        public BufferedImage parseUserSkin(BufferedImage var1) {
			        	return parseCape(var1, uuid);
			        }
			        
					public void func_152634_a() {}
			    };
			    
			    ThreadDownloadImageData textureCape = new ThreadDownloadImageData(null, url, null, iib);
			    textureManager.loadTexture(rl, textureCape);
		    }
		}
	}
		  
	public static BufferedImage parseCape(BufferedImage img, String uuid) {
		int imageWidth = 64;
		int imageHeight = 32;
		    
		BufferedImage srcImg = img;
		int srcWidth = srcImg.getWidth();
		int srcHeight = srcImg.getHeight();
		while ((imageWidth < srcWidth) || (imageHeight < srcHeight)) {
		      imageWidth *= 2;
		      imageHeight *= 2;
		}		  
		
		BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, 2);
		Graphics g = imgNew.getGraphics();
		
		g.drawImage(img, 0, 0, null);				
		g.dispose();	
		PlayerInfo.playersCape.put(uuid, true);
	
		return imgNew;
	}
	
	 public static boolean resourceExists(ResourceLocation resourceLocation) { 		 
		 try { 
			 IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation); 
			 if (resource != null) 
				 return true;
	 	} catch (IOException e) {} 
	 	return false; 
	 }
}


