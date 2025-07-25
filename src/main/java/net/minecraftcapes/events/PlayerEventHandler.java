package net.minecraftcapes.events;

import com.google.gson.Gson;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.player.PlayerHandler;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static net.minecraftcapes.MinecraftCapes.MINECRAFT_VERSION;

public class PlayerEventHandler {

	@SubscribeEvent
	public void onPlayerJoin(EntityJoinWorldEvent event) {
		if(event.entity instanceof EntityPlayer && event.world.isRemote) {
			EntityPlayer player = (EntityPlayer) event.entity;
			PlayerHandler playerHandler = PlayerHandler.getFromPlayer(player);
			if (playerHandler.getHasInfo()) return;

			// Download
			downloadProfile(playerHandler);
		}
	}

	/**
	 * Downloads a profile for a specific PlayerHandler.
	 * @param playerHandler
	 */
	public static void downloadProfile(PlayerHandler playerHandler) {
		playerHandler.setHasInfo(true);
		Thread playerDownload = new Thread(() -> {
			byte[] playerDataBytes = downloadData("https://api.minecraftcapes.net/profile/" + playerHandler.getPlayerUUID().toString().replace("-", ""));
			if (playerDataBytes == null) return;

			try {
				String json = new String(playerDataBytes, StandardCharsets.UTF_8);
				ProfileResult profileResult = new Gson().fromJson(json, ProfileResult.class);

				playerHandler.setHasCapeGlint(profileResult.capeGlint);
				playerHandler.setUpsideDown(profileResult.upsideDown);

				// Download cape image if available
				if (profileResult.cape_url != null) {
					byte[] capeBytes = downloadData(profileResult.cape_url);
					if (capeBytes != null) {
						BufferedImage capeImage = ImageIO.read(new ByteArrayInputStream(capeBytes));
						playerHandler.applyCape(capeImage);
					}
				}

				// Download ears image if available
				if (profileResult.ear_url != null) {
					byte[] earsBytes = downloadData(profileResult.ear_url);
					if (earsBytes != null) {
						BufferedImage earsImage = ImageIO.read(new ByteArrayInputStream(earsBytes));
						playerHandler.applyEars(earsImage);
					}
				}
			} catch (IOException e) {
				playerHandler.setHasInfo(false);
				MinecraftCapes.getLogger().warn("Error downloading profile data", e);
			}
		});

		playerDownload.setDaemon(true);
		playerDownload.start();
	}



	/**
	 * Downloads the data for the profile
	 * @param url
	 * @return
	 */
	private static byte[] downloadData(String url) {
		HttpURLConnection httpURLConnection = null;
		URI uri = URI.create(url);

		try {
			MinecraftCapes.getLogger().info("Getting texture {}", url);
			httpURLConnection = (HttpURLConnection) uri.toURL().openConnection(Minecraft.getMinecraft().getProxy());
			httpURLConnection.setRequestProperty("User-Agent", "minecraftcapes-mod/" + MINECRAFT_VERSION);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(false);
			httpURLConnection.connect();

			if (httpURLConnection.getResponseCode() / 100 == 2) {
				try (InputStream inputStream = httpURLConnection.getInputStream()) {
					return IOUtils.toByteArray(inputStream); // Read fully before closing
				}
			} else {
				MinecraftCapes.getLogger().warn("minecraftcapes.net returned a {}", httpURLConnection.getResponseCode());
				return null;
			}
		} catch (IOException e) {
			MinecraftCapes.getLogger().warn("No connection to minecraftcapes.net detected");
			throw new RuntimeException(e);
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
	}


	private static class ProfileResult {
		private boolean capeGlint = false;
		private boolean upsideDown = false;
		private String cape_url = null;
		private String ear_url = null;
	}
}
