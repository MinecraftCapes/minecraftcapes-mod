package co.uk.minecraftcapes.player.downloader;

import co.uk.minecraftcapes.helpers.Downloader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import static co.uk.minecraftcapes.reference.Reference.MODID;

public class DeleteElytra {
	
	public static void delete() {
		ResourceLocation blankElytra = new ResourceLocation(MODID, "empty.png");
		ResourceLocation defaultElytra = new ResourceLocation("textures/entity/elytra.png");		
	    TextureManager textureManager = Minecraft.getInstance().getTextureManager();	    
	    Downloader blankElytraObject = new Downloader(null, blankElytra, new ImageBufferDownload());
	    textureManager.loadTexture(defaultElytra, (ITextureObject) blankElytraObject);
	}

}
