package net.minecraftcapes.events;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.player.PlayerHandler;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class PlayerEventHandler {

	@SubscribeEvent
	public void onPlayerJoin(EntityJoinWorldEvent event) {
		if(event.getEntity() instanceof EntityPlayer && event.getWorld().isRemote()) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			PlayerHandler playerHandler = PlayerHandler.getFromPlayer(player);
			if(playerHandler == null || playerHandler.getHasInfo()) return;

			downloadProfile(playerHandler);
		}
	}

	public static void downloadProfile(PlayerHandler playerHandler) {
		Thread playerDownload = new Thread(() -> {
			try {
				MinecraftCapes.getLogger().debug("Getting profile for {}", playerHandler.getPlayerUUID());
				URL url = new URL("https://minecraftcapes.net/profile/" + playerHandler.getPlayerUUID().toString().replace("-", ""));
				HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection(Minecraft.getInstance().getProxy());
				httpurlconnection.setDoInput(true);
				httpurlconnection.setDoOutput(false);
				httpurlconnection.connect();

				if (httpurlconnection.getResponseCode() / 100 == 2) {
					Reader reader = new InputStreamReader(httpurlconnection.getInputStream(), "UTF-8");
					ProfileResult profileResult = new Gson().fromJson(reader, ProfileResult.class);
					reader.close();

					playerHandler.setHasInfo(true);
					playerHandler.setHasCapeGlint(profileResult.capeGlint);
					playerHandler.setUpsideDown(profileResult.upsideDown);

					if (profileResult.textures.get("cape") != null) {
						playerHandler.applyCape(profileResult.textures.get("cape"));
					}

					if (profileResult.textures.get("ears") != null) {
						playerHandler.applyEars(profileResult.textures.get("ears"));
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
		private boolean capeGlint = false;
		private boolean upsideDown = false;
		private Map<String, String> textures = null;
	}
}
