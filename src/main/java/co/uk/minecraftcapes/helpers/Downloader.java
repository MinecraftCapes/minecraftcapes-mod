package co.uk.minecraftcapes.helpers;

import co.uk.minecraftcapes.MinecraftCapes;
import co.uk.minecraftcapes.capabilities.PlayerHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
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
                        //If PNG (Static Cape) else GIF (Animated)
                        if(httpurlconnection.getContentType().equalsIgnoreCase("image/png")) {
                            NativeImage nativeImage;
                            nativeImage = NativeImage.read(httpurlconnection.getInputStream());
                            nativeImage = Downloader.this.imageBuffer.parseTexture(nativeImage, playerHandler);

                            Downloader.this.setNativeImage(nativeImage);

                            MinecraftCapes.getLogger().debug("Downloading complete. Image loaded in {}", nativeImage);
                            return;
                        } else if(httpurlconnection.getContentType().equalsIgnoreCase("image/gif")) {
                            ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
                            ImageInputStream imageInputStream = ImageIO.createImageInputStream(httpurlconnection.getInputStream());
                            reader.setInput(imageInputStream);

                            BufferedImage mergedImg = null;
                            Int2ObjectMap<NativeImage> animatedCape = new Int2ObjectOpenHashMap<>();
                            for(int i = 0; i < reader.getNumImages(true); i++) {
                                //Gets the current image and the previous image (if any)
                                BufferedImage newImg = reader.read(i);
                                if(i == 0) mergedImg = new BufferedImage(newImg.getWidth(), newImg.getHeight(), BufferedImage.TYPE_INT_ARGB);

                                //Merges the old and new image together. Otherwise you get a corrupt cape
                                mergedImg.getGraphics().drawImage(newImg, 0, 0, null);

                                //Creates a NativeImage from the BufferedImage and adds it to the map
                                NativeImage nativeImage = new NativeImage(mergedImg.getWidth(), mergedImg.getHeight(), true);
                                for (int x = 0; x < mergedImg.getWidth(); x++) {
                                    for (int y = 0; y < mergedImg.getHeight(); y++) {
                                        Color color = new Color(mergedImg.getRGB(x, y), true);
                                        nativeImage.setPixelRGBA(x, y, NativeImage.getCombined(color.getAlpha(), color.getBlue(), color.getGreen(), color.getRed()));
                                    }
                                }
                                animatedCape.put(i, nativeImage);
                            }
                            playerHandler.setAnimatedCape(animatedCape);

                            MinecraftCapes.getLogger().debug("Downloading complete. Animated Image loaded");
                            return;
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

    private static BufferedImage makeImageForIndex(BufferedImage oldImg, BufferedImage newImg) {
        BufferedImage mergedImg = new BufferedImage(oldImg.getWidth(), oldImg.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        newImg.getGraphics().drawImage(oldImg, 0, 0, null);

        return newImg;
    }
}