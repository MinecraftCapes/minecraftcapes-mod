package net.minecraftcapes.helpers;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.texture.TextureUtil;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.player.PlayerHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader extends ResourceTexture {

    private final String imageUrl;
    private final ImageFilter imageBuffer;
    private NativeImage nativeImage;
    private boolean textureUploaded;
    private PlayerHandler playerHandler;

    public Downloader(String imageUrlIn, Identifier textureResourceLocation, ImageFilter imageBufferIn, PlayerHandler playerHandler) {
        super(textureResourceLocation);
        this.imageUrl = imageUrlIn;
        this.imageBuffer = imageBufferIn;
        this.playerHandler = playerHandler;
    }


    private void checkTextureUploaded() {
        if (!this.textureUploaded) {
            if (this.nativeImage != null) {
                if (this.location != null) {
                    this.clearGlId();
                }

                TextureUtil.prepareImage(super.getGlId(), this.nativeImage.getWidth(), this.nativeImage.getHeight());
                this.nativeImage.upload(0, 0, 0, false);
                this.textureUploaded = true;
            }
        }
    }

    public int getGlId() {
        this.checkTextureUploaded();
        return super.getGlId();
    }

    public void setNativeImage(NativeImage nativeImageIn) {
        this.nativeImage = nativeImageIn;
    }

    public void load(ResourceManager resourceManager) throws IOException {
        if (this.nativeImage == null && this.location != null) {
            super.load(resourceManager);
        } else {
            this.loadTextureFromServer();
        }
    }

    protected void loadTextureFromServer() {
        if(Downloader.this.imageUrl == null) {
            return;
        }

        HttpURLConnection httpurlconnection = null;
        MinecraftCapes.getLogger().debug("Downloading http texture from {}", Downloader.this.imageUrl);

        try {
            httpurlconnection = (HttpURLConnection)(new URL(Downloader.this.imageUrl)).openConnection(MinecraftClient.getInstance().getNetworkProxy());
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(false);
            httpurlconnection.connect();

            if (httpurlconnection.getResponseCode() / 100 == 2) {
                if(httpurlconnection.getContentType().equalsIgnoreCase("image/png")) {
                    NativeImage nativeImage = NativeImage.read(httpurlconnection.getInputStream());

                    //If animated cape
                    if(nativeImage.getHeight() != nativeImage.getWidth() / 2) {
                        Int2ObjectMap<NativeImage> animatedCape = new Int2ObjectOpenHashMap<>();
                        int totalFrames = nativeImage.getHeight() / (nativeImage.getWidth() / 2);
                        for(int currentFrame = 0; currentFrame < totalFrames; currentFrame++) {
                            NativeImage frame = new NativeImage(nativeImage.getWidth(), nativeImage.getWidth() / 2, true);
                            for (int x = 0; x < frame.getWidth(); x++) {
                                for (int y = 0; y < frame.getHeight(); y++) {
                                    frame.setPixelRgba(x, y, nativeImage.getPixelRgba(x, y + (currentFrame * (nativeImage.getWidth() / 2))));
                                }
                            }
                            animatedCape.put(currentFrame, frame);
                        }
                        playerHandler.setAnimatedCape(animatedCape);
                        MinecraftCapes.getLogger().debug("Downloading complete. Animated cape loaded for {}", playerHandler.getPlayerUUID());
                        return;
                    } else {
                        nativeImage = Downloader.this.imageBuffer.filterImage(nativeImage, playerHandler);
                        Downloader.this.setNativeImage(nativeImage);
                        MinecraftCapes.getLogger().debug("Downloading complete. Image loaded in {}", nativeImage);
                        return;
                    }
                }
            }
        } catch (Exception exception) {
            MinecraftCapes.getLogger().error("Couldn't download http texture", (Throwable)exception);
            return;
        } finally {
            if (httpurlconnection != null) {
                MinecraftCapes.getLogger().debug("Disconnected from {}", httpurlconnection.getURL().toString());
                httpurlconnection.disconnect();
            }
        }
    }
}