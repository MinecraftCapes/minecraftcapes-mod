package net.minecraftcapes.events;

import com.google.gson.Gson;
import net.minecraft.client.MinecraftClient;
import net.minecraftcapes.player.PlayerHandler;
import net.minecraftcapes.player.downloader.DownloadCape;
import net.minecraftcapes.player.downloader.DownloadEars;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class PlayerEventHandler {

	public static void onPlayerJoin(UUID uuid) {
		PlayerHandler playerHandler = PlayerHandler.getFromPlayer(uuid);
		if(playerHandler == null || playerHandler.getHasInfo()) return;

		Thread playerDownload = new Thread(() -> {
			try {
				//Todo needs changing to .net
				URL url = new URL("https://minecraftcapes.co.uk/profile/" + playerHandler.getPlayerUUID().toString().replace("-", ""));
				HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection(MinecraftClient.getInstance().getNetworkProxy());
				httpurlconnection.setDoInput(true);
				httpurlconnection.setDoOutput(false);
				httpurlconnection.connect();

				if (httpurlconnection.getResponseCode() / 100 == 2) {
					Reader reader = new InputStreamReader(httpurlconnection.getInputStream(), "UTF-8");
					ProfileResult profileResult = new Gson().fromJson(reader, ProfileResult.class);

					playerHandler.setHasInfo(true);
					playerHandler.setUpsideDown(profileResult.upsideDown);

					if (profileResult.cape != null) {
						DownloadCape.download(profileResult.cape, playerHandler);
					}

					if (profileResult.ears != null) {
						DownloadEars.download(profileResult.ears, playerHandler);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		playerDownload.setDaemon(true);
		playerDownload.start();
	}

	class ProfileResult {
		private String cape = null;
		private String ears = null;
		private boolean upsideDown = false;
	}

}
