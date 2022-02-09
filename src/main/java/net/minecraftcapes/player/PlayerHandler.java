package net.minecraftcapes.player;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.PlayerBase;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.helpers.GetMinecraftInstance;
import net.minecraftcapes.helpers.MinecraftApi;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

public class PlayerHandler {

    private static final HashMap<String, PlayerHandler> instances = new HashMap<>();

    @Getter private BufferedImage cape;
    @Getter private BufferedImage ears;

    @Getter @Setter private Boolean hasStaticCape = false;
    @Getter @Setter private Boolean hasAnimatedCape = false;
    @Getter @Setter private Boolean showCape = true;
    @Getter @Setter private Boolean forceShowElytra = false;
    @Getter @Setter private Boolean forceHideElytra = false;
    @Getter @Setter private Boolean hasCapeGlint = false;
    @Getter @Setter private boolean upsideDown = false;
    @Getter @Setter private Boolean hasInfo = false;
    @Setter @Getter private UUID playerUUID;

    @Getter
    private HashMap<Integer, BufferedImage> animatedCape;

    //Animated Cape Settings
    private long lastFrameTime = 0;
    private int lastFrame = 0;

    public PlayerHandler(PlayerBase player) {
        this.playerUUID = MinecraftApi.getUUID(player.name);
        PlayerHandler.instances.put(player.name, this);
    }

    /**
     * Tries to get the PlayerHandler instance from a player
     * @param player The player entity
     * @return Returns the current instance of PlayerHandler
     */
    public static PlayerHandler getFromPlayer(PlayerBase player) {
        PlayerHandler playerHandler = PlayerHandler.instances.get(player.name);
        return playerHandler == null ? new PlayerHandler(player) : playerHandler;
    }

    /**
     * Reads a base64 string and converts it to a NativeImage
     * @param textureBase64 The base64 texture to read
     * @return Returns a Buffered Image
     */
    private BufferedImage readTexture(String textureBase64) {
        try {
            byte[] imgBytes = Base64.getDecoder().decode(textureBase64);
            ByteArrayInputStream bias = new ByteArrayInputStream(imgBytes);
            return ImageIO.read(bias);
        } catch (IOException e) {
            MinecraftCapes.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return new BufferedImage(0, 0, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Gets the cape texture and resizes or splits it accordingly
     * @param cape The Cape base64 texture to parse
     */
    public void applyCape(String cape) {
        BufferedImage capeImage = readTexture(cape);
        //If the height is not 1/2 the width (32 == 64/2) then its an animated cape
        if(capeImage.getHeight() != capeImage.getWidth() / 2) {
            HashMap<Integer, BufferedImage> animatedCapeFrames = new HashMap<>();
            int totalFrames = capeImage.getHeight() / (capeImage.getWidth() / 2);
            for(int currentFrame = 0; currentFrame < totalFrames; currentFrame++) {
                BufferedImage frame = new BufferedImage(capeImage.getWidth(), capeImage.getWidth() / 2, BufferedImage.TYPE_INT_ARGB);
                Graphics frameGraphics = frame.getGraphics();
                frameGraphics.drawImage(capeImage,
                        0,
                        0,
                        capeImage.getWidth(),
                        (capeImage.getWidth() / 2),
                        0,
                        (currentFrame * (capeImage.getWidth() / 2)),
                        capeImage.getWidth(),
                        ((currentFrame + 1) * (capeImage.getWidth() / 2)),
                        null);
                frameGraphics.dispose();
                animatedCapeFrames.put(currentFrame, frame);
            }

            MinecraftCapes.getLogger().debug("Setting animated cape for {}", playerUUID);
            this.cape = animatedCapeFrames.get(0);
            this.animatedCape = animatedCapeFrames;
            this.setHasAnimatedCape(true);
        } else {
            int imageWidth = 64;
            int imageHeight = 32;

            int srcWidth = capeImage.getWidth(), srcHeight = capeImage.getHeight();
            while (imageWidth < srcWidth || imageHeight < srcHeight) {
                imageWidth *= 2;
                imageHeight *= 2;
            }

            final BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics g = imgNew.getGraphics();
            g.drawImage(capeImage, 0, 0, null);
            g.dispose();

            this.cape = imgNew;
            this.setHasStaticCape(true);
            this.setHasAnimatedCape(false);
            MinecraftCapes.getLogger().debug("Static cape loaded for {}", playerUUID);
        }
    }


    public void applyEars(String ears) {
        BufferedImage earImage = readTexture(ears);
        BufferedImage imgNew = new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics g = imgNew.getGraphics();
        g.drawImage(earImage, 24, 0, null);
        g.dispose();
        this.ears = imgNew;
    }

    /**
     * Gets the current frame for the player
     * @return Identifier
     */
    private BufferedImage getCapeFrame() {
        if(this.hasAnimatedCape) {
            final long time = System.currentTimeMillis();
            int capeInterval = 100;
            if (time > lastFrameTime + capeInterval) {
                int currentFrameNo = (lastFrame + 1 > getAnimatedCape().size() - 1) ? 0 : lastFrame + 1;

                lastFrame = currentFrameNo;
                lastFrameTime = time;

                this.cape = getAnimatedCape().get(currentFrameNo);
            }
        }
        return this.cape;
    }

    /**
     * Returns the player's current cape resource
     */
    public void bindCape() {
        GetMinecraftInstance.getMinecraftInstance().textureManager.method_1088(this.getCapeFrame());
    }

    /**
     * Returns the player's ear resource
     */
    public void bindEars() {
        GetMinecraftInstance.getMinecraftInstance().textureManager.method_1088(this.ears);
    }
}
