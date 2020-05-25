package net.minecraftcapes.helpers;

import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.player.PlayerHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

public class Downloader extends SimpleTexture {

    private static final AtomicInteger TEXTURE_DOWNLOADER_THREAD_ID = new AtomicInteger(0);
    private final String imageUrl;
    @Nullable
    private final IImageBuffer imageBuffer;
    @Nullable
    private NativeImage nativeImage;
    @Nullable
    private Thread imageThread;
    private boolean textureUploaded;
    private PlayerHandler playerHandler;
    
    public Downloader(String imageUrlIn, ResourceLocation textureResourceLocation, IImageBuffer imageBufferIn, PlayerHandler playerHandler) {
        super(textureResourceLocation);
        this.imageUrl = imageUrlIn;
        this.imageBuffer = imageBufferIn;
        this.playerHandler = playerHandler;
    }

    
    private void checkTextureUploaded() {
        if (!this.textureUploaded) {
            if (this.nativeImage != null) {
                if (this.textureLocation != null) {
                    this.deleteGlTexture();
                }

                TextureUtil.prepareImage(super.getGlTextureId(), this.nativeImage.getWidth(), this.nativeImage.getHeight());
                this.nativeImage.uploadTextureSub(0, 0, 0, false);
                this.textureUploaded = true;
            }
        }
    }
        
    public int getGlTextureId() {
    	this.checkTextureUploaded();
    	return super.getGlTextureId();
    }
    
    public void setNativeImage(NativeImage nativeImageIn)
    {
        this.nativeImage = nativeImageIn;
    }    

    public void loadTexture(IResourceManager resourceManager) throws IOException
    {
        if (this.nativeImage == null && this.textureLocation != null) {
            super.loadTexture(resourceManager);
        }

        if (this.imageThread == null) {
            this.loadTextureFromServer();
        }
    }

    protected void loadTextureFromServer() {
    	if(Downloader.this.imageUrl == null) {
    		return;
    	}
    	
        this.imageThread = new Thread("Texture Downloader #" + TEXTURE_DOWNLOADER_THREAD_ID.incrementAndGet()) {
            public void run() {
                HttpURLConnection httpurlconnection = null;
                MinecraftCapes.getLogger().debug("Downloading http texture from {}", Downloader.this.imageUrl);

                try {
                    httpurlconnection = (HttpURLConnection)(new URL(Downloader.this.imageUrl)).openConnection(Minecraft.getInstance().getProxy());
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
                                            frame.setPixelRGBA(x, y, nativeImage.getPixelRGBA(x, y + (currentFrame * (nativeImage.getWidth() / 2))));
                                        }
                                    }
                                    animatedCape.put(currentFrame, frame);
                                }
                                playerHandler.setAnimatedCape(animatedCape);
                                MinecraftCapes.getLogger().debug("Downloading complete. Animated cape loaded for {}", playerHandler.getPlayerUUID());
                                return;
                            } else {
                                nativeImage = Downloader.this.imageBuffer.parseTexture(nativeImage, playerHandler);
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
        };
        this.imageThread.setDaemon(true);
        this.imageThread.start();
    }
}