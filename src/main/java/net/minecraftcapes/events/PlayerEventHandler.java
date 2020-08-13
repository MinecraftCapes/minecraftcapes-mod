package net.minecraftcapes.events;

import com.google.gson.Gson;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.player.PlayerHandler;
import net.minecraftforge.client.event.RenderLivingEvent;
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
		if(event.getEntity() instanceof PlayerEntity && event.getWorld().isRemote()) {
			PlayerEntity player = (PlayerEntity) event.getEntity();
			PlayerHandler playerHandler = PlayerHandler.getFromPlayer(player);
			if(playerHandler == null || playerHandler.getHasInfo()) return;

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
		private boolean capeGlint = false;
		private boolean upsideDown = false;
		private Map<String, String> textures = null;
	}
}
