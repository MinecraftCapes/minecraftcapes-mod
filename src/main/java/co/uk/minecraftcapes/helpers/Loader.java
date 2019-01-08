package co.uk.minecraftcapes.helpers;

import co.uk.minecraftcapes.events.PlayerEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import static co.uk.minecraftcapes.reference.Reference.MODID;

public class Loader {

    public static void download(final String uuid, String type, int width, int height) {

        if ((uuid != null) && (!uuid.isEmpty())) {

            //String url = "https://www.minecraftcapes.co.uk/get" + StringUtils.capitalize(type) + ".php?uuid=" + uuid;
            String url = "https://www.minecraftcapes.co.uk/get" + StringUtils.capitalize(type) + ".php?uuid=ba4161c03a42496c8ae07d13372f3371";
            ResourceLocation rl = new ResourceLocation(MODID, type.toLowerCase() + "/" + uuid);
            TextureManager textureManager = Minecraft.getInstance().getTextureManager();

            IImageBuffer iib = new IImageBuffer() {
                public NativeImage parseUserSkin(NativeImage img) {
                    return parse(img, uuid, type, width, height);
                }

                public void skinAvailable() {}
            };

            Downloader textureEars = new Downloader(url, null, iib);
            textureManager.loadTexture(rl, textureEars);
        }
    }

    public static NativeImage parse(NativeImage img, String uuid, String type, int width, int height) {

        for (int srcWidth = img.getWidth(), srcHeight = img.getHeight(); width < srcWidth || height < srcHeight; width *= 2, height *= 2) {}

        final NativeImage imgNew = new NativeImage(width, height, true);

        imgNew.copyImageData(img);
        img.close();

        PlayerEventHandler.playersCape.put(uuid, true);

        if (type == "Cape"){
            PlayerEventHandler.playersCape.put(uuid, true);
        }else if(type == "Ears"){
            PlayerEventHandler.playersEar.put(uuid, true);
        }
        return imgNew;

    }
}
