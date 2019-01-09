package co.uk.minecraftcapes.player.downloader;

import co.uk.minecraftcapes.events.PlayerEventHandler;
import co.uk.minecraftcapes.helpers.Downloader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import static co.uk.minecraftcapes.reference.Reference.MODID;

public class DownloadCape {
	
	public static void download(final String uuid) {
		
	    if ((uuid != null) && (!uuid.isEmpty())) {

	    	//String url = "https://www.MinecraftCapes.co.uk/getCape.php?uuid=" + uuid;
	    	String url = "https://www.MinecraftCapes.co.uk/getCape.php?uuid=ba4161c03a42496c8ae07d13372f3371";
	    	
		    ResourceLocation rl = new ResourceLocation(MODID, "capes/" + uuid);
		    TextureManager textureManager = Minecraft.getInstance().getTextureManager();
		                
		    IImageBuffer iib = new IImageBuffer() {	    		
				public NativeImage parseUserSkin(NativeImage img) {
	    			return parseCape(img, uuid);
				}

				public void skinAvailable() {}
		    };
		                		    		  
            Downloader textureCape = new Downloader(url, null, iib);
            textureManager.loadTexture(rl, textureCape);
		}
	}
		  
	public static NativeImage parseCape(NativeImage img, String uuid) {		
        int imageWidth = 64;
        int imageHeight = 32;
        
        for (int srcWidth = img.getWidth(), srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageWidth *= 2, imageHeight *= 2) {}
        
        final NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
        
        imgNew.copyImageData(img);
        img.close();
        
        PlayerEventHandler.playersCape.put(uuid, true);
        
        return imgNew;
	}
}