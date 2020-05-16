package co.uk.minecraftcapes.events;

import co.uk.minecraftcapes.helpers.PlayerHandler;
import co.uk.minecraftcapes.player.downloader.DownloadCape;
import co.uk.minecraftcapes.player.downloader.DownloadEars;
import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class PlayerEventHandler {

	@SubscribeEvent
	public void onPlayerJoin(EntityJoinWorldEvent event) {
		if(event.getEntity() instanceof PlayerEntity) {
			UUID playerUUID = event.getEntity().getUniqueID();
			PlayerHandler playerHandler = PlayerHandler.getPlayer(playerUUID);
			if(playerHandler.getHasInfo()) return;

			new Thread(() -> {
				try {
					URL url = new URL("https://minecraftcapes.co.uk/getProfile/" + playerUUID);
					HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection(Minecraft.getInstance().getProxy());
					httpurlconnection.setDoInput(true);
					httpurlconnection.setDoOutput(false);
					httpurlconnection.connect();

					if (httpurlconnection.getResponseCode() / 100 == 2) {
						Reader reader = new InputStreamReader(httpurlconnection.getInputStream(), "UTF-8");
						ProfileResult profileResult = new Gson().fromJson(reader, ProfileResult.class);
						playerHandler.setHasInfo(true);

						if (profileResult.cape != null) {
							DownloadCape.download(profileResult.cape, playerUUID);
						}

						if (profileResult.ears != null) {
							DownloadEars.download(profileResult.ears, playerUUID);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}

	class ProfileResult {
		private String cape = null;
		private String ears = null;
		private boolean upsideDown = false;
	}
}
