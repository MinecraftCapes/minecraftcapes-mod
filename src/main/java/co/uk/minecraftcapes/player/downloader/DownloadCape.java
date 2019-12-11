package co.uk.minecraftcapes.player.downloader;

import static co.uk.minecraftcapes.reference.Reference.MODID;

import co.uk.minecraftcapes.events.PlayerEventHandler;
import co.uk.minecraftcapes.helpers.Downloader;
import co.uk.minecraftcapes.helpers.ImageFilter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

public class DownloadCape {
	
	public static void download(final String uuid) {
		
	    if ((uuid != null) && (!uuid.isEmpty())) {

	    	String url = "https://MinecraftCapes.co.uk/getCape/" + uuid;	   
	    	
	    	Identifier rl = new Identifier(MODID, "capes/" + uuid);
		    TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
		                
		    ImageFilter imgFilter = new ImageFilter() {	    		
				@Override
				public NativeImage filterImage(NativeImage img) {
					return parseCape(img, uuid);
				}
		    };
		                				    
		    Downloader textureCape = new Downloader(url, null, imgFilter);
            textureManager.registerTexture(rl, textureCape);
		}
	}
		  
	public static NativeImage parseCape(NativeImage img, String uuid) {		
        int imageWidth = 64;
        int imageHeight = 32;
        
        for (int srcWidth = img.getWidth(), srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageWidth *= 2, imageHeight *= 2) {}
        
        final NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
        
        imgNew.copyFrom(img);
        img.close();
        
        PlayerEventHandler.playersCape.put(uuid, true);
        
        return imgNew;
	}
}