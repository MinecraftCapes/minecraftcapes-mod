package co.uk.minecraftcapes.player.downloader;

import co.uk.minecraftcapes.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class DeleteElytra {
	
	public static void delete() {
		ResourceLocation blankElytra = new ResourceLocation(Reference.MOD_ID, "empty.png");
		ResourceLocation defaultElytra = new ResourceLocation("textures/entity/elytra.png");		
	    TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();	    
	    ThreadDownloadImageData blankElytraObject = new ThreadDownloadImageData(null, null, blankElytra, new ImageBufferDownload());
	    textureManager.loadTexture(defaultElytra, blankElytraObject);
	}

}
