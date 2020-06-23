package net.minecraftcapes.events;

import com.google.gson.Gson;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftcapes.player.PlayerHandler;
import net.minecraftcapes.player.downloader.DownloadCape;
import net.minecraftcapes.player.downloader.DownloadEars;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlayerEventHandler {

	@SubscribeEvent
	public void onPlayerJoin(EntityJoinWorldEvent event) {
		if(event.getEntity() instanceof PlayerEntity && event.getWorld().isRemote()) {
			PlayerEntity player = (PlayerEntity) event.getEntity();
			PlayerHandler playerHandler = PlayerHandler.getFromPlayer(player);
			if(playerHandler == null || playerHandler.getHasInfo()) return;

			Thread playerDownload = new Thread(() -> {
				try {
					//Todo needs changing to .net
					URL url = new URL("https://minecraftcapes.co.uk/profile/" + playerHandler.getPlayerUUID().toString().replace("-", ""));
					HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection(Minecraft.getInstance().getProxy());
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
	}

	@SubscribeEvent
	public void onPlayerRender(RenderLivingEvent.Pre event) {
		if(event.getEntity() instanceof PlayerEntity) {
			MatrixStack matrixStack = event.getMatrixStack();
			PlayerEntity playerEntity = (PlayerEntity) event.getEntity();
			PlayerHandler playerHandler = PlayerHandler.getFromPlayer(playerEntity);

			if(playerHandler.isUpsideDown()) {
				matrixStack.push();
				matrixStack.translate(0.0D, playerEntity.getHeight() + 0.1F, 0.0D);

				//Rotates the player upside down
				matrixStack.rotate(Vector3f.XN.rotationDegrees(180F));

				//Removes the current rotation then negates it
				matrixStack.rotate(Vector3f.YN.rotationDegrees(-playerEntity.rotationYaw));
				matrixStack.rotate(Vector3f.YN.rotationDegrees(-playerEntity.rotationYaw));

				//Flips the rotation again
				matrixStack.rotate(Vector3f.YN.rotationDegrees(180F));
			}
		}
	}

	@SubscribeEvent
	public void afterPlayerRender(RenderLivingEvent.Post event) {
		if(event.getEntity() instanceof PlayerEntity) {
			MatrixStack matrixStack = event.getMatrixStack();
			PlayerEntity playerEntity = (PlayerEntity) event.getEntity();
			PlayerHandler playerHandler = PlayerHandler.getFromPlayer(playerEntity);

			if(playerHandler.isUpsideDown()) {
				matrixStack.translate(0.0D, -playerEntity.getHeight() - 0.1F, 0.0D);
				matrixStack.pop();
			}
		}
	}

	class ProfileResult {
		private String cape = null;
		private String ears = null;
		private boolean upsideDown = false;
	}
}
