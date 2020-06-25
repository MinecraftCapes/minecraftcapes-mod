package net.minecraftcapes.player.downloader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftcapes.helpers.Downloader;
import net.minecraftcapes.helpers.IImageBuffer;
import net.minecraftcapes.player.PlayerHandler;

import java.util.UUID;

import static net.minecraftcapes.MinecraftCapes.MODID;

public class DownloadEars {

	public static void download(String earsUrl, PlayerHandler playerHandler) {
		UUID playerUUID = playerHandler.getPlayerUUID();
		if(playerUUID != null) {
	    	ResourceLocation rl = new ResourceLocation(MODID, "ears/" + playerUUID);
	    	TextureManager textureManager = Minecraft.getInstance().getTextureManager();
	    	Downloader textureEars = new Downloader(earsUrl, playerHandler.getEarLocation(), iImageBuffer, playerHandler);
	    	textureManager.loadTexture(rl, textureEars);
		}
	}

	private static final IImageBuffer iImageBuffer = (img, playerHandler) -> {
		playerHandler.setHasEars(true);
		return img;
	};
}