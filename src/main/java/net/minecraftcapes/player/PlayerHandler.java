package net.minecraftcapes.player;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftcapes.MinecraftCapes;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static net.minecraftcapes.MinecraftCapes.MODID;

public class PlayerHandler {

    private static HashMap<UUID, PlayerHandler> instances = new HashMap<>();

    @Setter private boolean hasStaticCape = false;
    @Setter private boolean hasEars = false;
    @Setter private boolean hasAnimatedCape = false;
    @Getter @Setter private Boolean showCape = true;
    @Getter @Setter private Boolean hasCapeGlint = false;
    @Getter @Setter private boolean upsideDown = false;
    @Getter @Setter private Boolean hasInfo = false;
    @Setter @Getter private UUID playerUUID;

    @Getter
    private Int2ObjectMap<BufferedImage> animatedCape;

    //Animated Cape Settings
    private long lastFrameTime = 0;
    private int lastFrame = 0;
    private int capeInterval = 100;

    public PlayerHandler(EntityPlayer player) {
        this.playerUUID = player.getUniqueID();
        PlayerHandler.instances.put(playerUUID, this);
    }

    /**
     * Tries to get the PlayerHandler instance from a player
     * @param player
     * @return
     */
    public static PlayerHandler getFromPlayer(EntityPlayer player) {
        PlayerHandler playerHandler = PlayerHandler.instances.get(player.getUniqueID());
        return playerHandler == null ? new PlayerHandler(player) : playerHandler;
    }

    /**
     * Reads a base64 string and converts it to a BufferedImage
     * @param textureBase64
     * @return
     */
    private BufferedImage readTexture(String textureBase64) {
        try {
            byte[] imgBytes = Base64.decodeBase64(textureBase64);
            ByteArrayInputStream bias = new ByteArrayInputStream(imgBytes);
            return ImageIO.read(bias);
        } catch (IOException e) {
            MinecraftCapes.getLogger().error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the cape texture and resizes or splits it accordingly
     * @param cape
     */
    public void applyCape(String cape) {
        BufferedImage capeImage = readTexture(cape);
        //If the height is not 1/2 the width (32 == 64/2) then its an animated cape
        if(capeImage.getHeight() != capeImage.getWidth() / 2) {
            Int2ObjectMap<BufferedImage> animatedCape = new Int2ObjectOpenHashMap<>();
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
                animatedCape.put(currentFrame, frame);
            }
            setAnimatedCape(animatedCape);
            MinecraftCapes.getLogger().debug("Animated cape loaded for {}", playerUUID);
        } else {
            int imageWidth = 64;
            int imageHeight = 32;

            for (int srcWidth = capeImage.getWidth(), srcHeight = capeImage.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageWidth *= 2, imageHeight *= 2) {}

            final BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics g = imgNew.getGraphics();
            g.drawImage(capeImage, 0, 0, null);
            g.dispose();

            applyTexture(new ResourceLocation(MODID, "capes/" + playerUUID), imgNew);
            setHasStaticCape(true);
            MinecraftCapes.getLogger().debug("Static cape loaded for {}", playerUUID);
        }
    }

    public void applyEars(String ears) {
        BufferedImage earImage;
        if(MinecraftCapes.isLabyMod()) {
            BufferedImage oldImage = readTexture(ears);
            earImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            Graphics g = earImage.getGraphics();
            g.drawImage(oldImage, 24, 0, null);
            g.dispose();
        } else {
            earImage = readTexture(ears);
        }
        applyTexture(new ResourceLocation(MODID, "ears/" + playerUUID), earImage);
        this.setHasEars(true);
    }

    /**
     * Sets the animated cape textures and loads all resources to memory
     * @param animatedCape
     */
    public void setAnimatedCape(Int2ObjectMap<BufferedImage> animatedCape) {
        MinecraftCapes.getLogger().debug("Setting animated cape for {}", playerUUID);
        this.animatedCape = animatedCape;
        this.setHasAnimatedCape(true);
        this.loadFramesToResource();
    }

    /**
     * Load all BufferedImages into a ResourceLocation
     */
    private void loadFramesToResource() {
        MinecraftCapes.getLogger().debug("Loading resources to memory for {}", playerUUID);
        getAnimatedCape().forEach((integer, BufferedImage) -> {
            ResourceLocation currentResource = new ResourceLocation(MODID, String.format("capes/%s/%d", playerUUID, integer));
            applyTexture(currentResource, BufferedImage);
        });
    }

    /**
     * Gets the current frame for the player
     * @return ResourceLocation
     */
    private ResourceLocation getFrame() {
        final long time = System.currentTimeMillis();
        if(time > lastFrameTime + capeInterval) {
            int currentFrameNo = (lastFrame + 1 > getAnimatedCape().size() - 1) ? 0 : lastFrame + 1;

            lastFrame = currentFrameNo;
            lastFrameTime = time;

            return new ResourceLocation(MODID, String.format("capes/%s/%d", playerUUID, currentFrameNo));
        }
        return new ResourceLocation(MODID, String.format("capes/%s/%d", playerUUID, lastFrame));
    }

    /**
     * Returns the player current cape resource
     * @return
     */
    public ResourceLocation getCapeLocation() {
        return hasStaticCape ? new ResourceLocation(MODID, "capes/" + playerUUID) : hasAnimatedCape ? getFrame() : null;
    }

    /**
     * Returns the players ear resource
     * @return
     */
    public ResourceLocation getEarLocation() {
        return hasEars ? new ResourceLocation(MODID, "ears/" + playerUUID) : null;
    }

    /**
     * Applys a texture on the render thread
     * @param resourceLocation
     * @param BufferedImage
     */
    private void applyTexture(ResourceLocation resourceLocation, BufferedImage BufferedImage) {
        Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, new DynamicTexture(BufferedImage)));
    }

    /**
     * A nice to string thing
     * @return
     */
    @Override
    public String toString() {
        return "PlayerHandler{" +
                "hasStaticCape=" + hasStaticCape +
                ", hasEars=" + hasEars +
                ", hasAnimatedCape=" + hasAnimatedCape +
                ", hasCapeGlint=" + hasCapeGlint +
                ", upsideDown=" + upsideDown +
                ", hasInfo=" + hasInfo +
                ", playerUUID=" + playerUUID +
                ", animatedCape=" + animatedCape +
                ", lastFrameTime=" + lastFrameTime +
                ", lastFrame=" + lastFrame +
                ", capeInterval=" + capeInterval +
                '}';
    }
}