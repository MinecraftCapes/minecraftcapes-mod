package co.uk.minecraftcapes.helpers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class Downloader extends SimpleTexture
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final AtomicInteger TEXTURE_DOWNLOADER_THREAD_ID = new AtomicInteger(0);
    private final String imageUrl;
    @Nullable
    private final IImageBuffer imageBuffer;
    @Nullable
    private NativeImage nativeImage;
    @Nullable
    private Thread imageThread;
    private boolean textureUploaded;
    
    public Downloader(String imageUrlIn, ResourceLocation textureResourceLocation, IImageBuffer imageBufferIn)
    {
        super(textureResourceLocation);
        this.imageUrl = imageUrlIn;
        this.imageBuffer = imageBufferIn;
    }

    
    private void checkTextureUploaded()
    {
        if (!this.textureUploaded)
        {
            if (this.nativeImage != null)
            {
                if (this.textureLocation != null)
                {
                    this.deleteGlTexture();
                }

                TextureUtil.allocateTexture(super.getGlTextureId(), this.nativeImage.getWidth(), this.nativeImage.getHeight());
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

        if (this.imageBuffer != null)
        {
            this.imageBuffer.skinAvailable();
        }
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException
    {
        if (this.nativeImage == null && this.textureLocation != null)
        {
            super.loadTexture(resourceManager);
        }

        if (this.imageThread == null)
        {
            this.loadTextureFromServer();
        }
    }

    protected void loadTextureFromServer()
    {
    	
    	if(Downloader.this.imageUrl == null) {
    		return;
    	}
    	
        this.imageThread = new Thread("Texture Downloader #" + TEXTURE_DOWNLOADER_THREAD_ID.incrementAndGet())
        {
            public void run()
            {
                HttpURLConnection httpurlconnection = null;
                Downloader.LOGGER.info("Downloading http texture from {}", Downloader.this.imageUrl);

                try
                {
                    httpurlconnection = (HttpURLConnection)(new URL(Downloader.this.imageUrl)).openConnection(Minecraft.getInstance().getProxy());
                    httpurlconnection.setDoInput(true);
                    httpurlconnection.setDoOutput(false);
                    httpurlconnection.connect();

                    if (httpurlconnection.getResponseCode() / 100 == 2)
                    {
                        NativeImage nativeImage;
                        nativeImage = NativeImage.read(httpurlconnection.getInputStream());                        
                        nativeImage = Downloader.this.imageBuffer.parseUserSkin(nativeImage);

                        Downloader.this.setNativeImage(nativeImage);
                        
                        Downloader.LOGGER.info("Downloading complete. Image loaded in {}", nativeImage);
                        
                        return;
                    }
                }
                catch (Exception exception)
                {
                    Downloader.LOGGER.error("Couldn't download http texture", (Throwable)exception);
                    return;
                }
                finally
                {
                    if (httpurlconnection != null)
                    {
                    	Downloader.LOGGER.info("Disconnected from {}", httpurlconnection.getURL().toString());
                        httpurlconnection.disconnect();
                    }
                }
            }
        };
        this.imageThread.setDaemon(true);
        this.imageThread.start();
    }
}