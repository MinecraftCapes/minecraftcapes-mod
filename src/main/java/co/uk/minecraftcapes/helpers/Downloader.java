package co.uk.minecraftcapes.helpers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.texture.TextureUtil;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class Downloader extends ResourceTexture
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final AtomicInteger TEXTURE_DOWNLOADER_THREAD_ID = new AtomicInteger(0);
    private final String imageUrl;
    private NativeImage nativeImage;
    private Thread imageThread;
    private boolean textureUploaded;
	private ImageFilter imageFilter;
    
    public Downloader(String imageUrlIn, Identifier textureIdentifier, ImageFilter imageFilterIn)
    {
        super(textureIdentifier);
        this.imageUrl = imageUrlIn;
        this.imageFilter = imageFilterIn;
    }
    
    private void checkTextureUploaded()
    {
        if (!this.textureUploaded)
        {
            if (this.nativeImage != null)
            {
                if (this.location != null)
                {
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
    
    public void setNativeImage(NativeImage nativeImageIn)
    {
        this.nativeImage = nativeImageIn;

        if (this.imageFilter != null)
        {
            //this.imageFilter.method_3238();
        }
    }

    public void load(ResourceManager resourceManager) throws IOException
    {
        if (this.nativeImage == null && this.location != null)
        {
            super.load(resourceManager);
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
                Downloader.LOGGER.debug("Downloading http texture from {}", Downloader.this.imageUrl);

                try
                {
                    httpurlconnection = (HttpURLConnection)(new URL(Downloader.this.imageUrl)).openConnection(MinecraftClient.getInstance().getNetworkProxy());
                    httpurlconnection.setDoInput(true);
                    httpurlconnection.setDoOutput(false);
                    httpurlconnection.connect();

                    if (httpurlconnection.getResponseCode() / 100 == 2)
                    {
                        NativeImage nativeImage;
                        nativeImage = NativeImage.read(httpurlconnection.getInputStream());                        
                        nativeImage = Downloader.this.imageFilter.filterImage(nativeImage);

                        Downloader.this.setNativeImage(nativeImage);
                        
                        Downloader.LOGGER.debug("Downloading complete. Image loaded in {}", nativeImage);                        
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
                    	Downloader.LOGGER.debug("Disconnected from {}", httpurlconnection.getURL().toString());
                        httpurlconnection.disconnect();
                    }
                }
            }
        };
        this.imageThread.setDaemon(true);
        this.imageThread.start();
    }
}