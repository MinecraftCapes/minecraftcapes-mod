package co.uk.minecraftcapes.player.downloader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class DeleteElytra {
	
	public static void delete() {
		ResourceLocation blankElytra = new ResourceLocation("jmcm", "empty.png");				
		ResourceLocation defaultElytra = new ResourceLocation("textures/entity/elytra.png");		
	    TextureManager textureManager = Minecraft.getInstance().getTextureManager();	    
	    ImageDownloader blankElytraObject = new ImageDownloader(null, blankElytra, new ImageBufferDownload());	    
	    textureManager.loadTexture(defaultElytra, (ITextureObject) blankElytraObject);
	}

}
