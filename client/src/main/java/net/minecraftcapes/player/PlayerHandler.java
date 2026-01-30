package net.minecraftcapes.player;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraftcapes.MinecraftCapes;
import org.lwjgl.Sys;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.UUID;

public class PlayerHandler {
    
    private static final HashMap<UUID, PlayerHandler> instances = new HashMap<>();
    
    @Setter private boolean hasStaticCape = false;
    @Setter private boolean hasEars = false;
    @Setter private boolean hasAnimatedCape = false;
    @Getter @Setter private Boolean hasCapeGlint = false;
    @Getter @Setter private boolean upsideDown = false;
    @Getter @Setter private Boolean hasInfo = false;
    @Setter @Getter private UUID playerUUID;

    private Int2IntArrayMap animatedCape;
    private int cape;
    private int ears;

    
    //Animated Cape Settings
    private long lastFrameTime = 0;
    private int lastFrame = 0;
    private int capeInterval = 100;
    
    public PlayerHandler(UUID uuid) {
        this.playerUUID = uuid;
        PlayerHandler.instances.put(playerUUID, this);
    }
    
    /**
     * Tries to get the PlayerHandler instance from a player
     * @param uuid the players uuid
     * @return The player handler
     */
    public static PlayerHandler get(UUID uuid) {
        PlayerHandler playerHandler = PlayerHandler.instances.get(uuid);
        return playerHandler == null ? new PlayerHandler(uuid) : playerHandler;
    }
    
    /**
     * Remove a player
     * @param uuid
     */
    public static void remove(UUID uuid) {
        instances.remove(uuid);
    }
    
    /**
     * Gets the cape texture and resizes or splits it accordingly
     * @param capeImage
     */
    public void applyCape(BufferedImage capeImage) {
        //If the height is not 1/2 the width (32 == 64/2) then its an animated cape
        if(capeImage.getHeight() != capeImage.getWidth() / 2) {
            Int2IntArrayMap animatedCape = new Int2IntArrayMap();
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
                animatedCape.put(currentFrame, this.applyTexture(frame));
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

            this.cape = this.applyTexture(imgNew);
            this.setHasStaticCape(true);
            this.setHasAnimatedCape(false);
            MinecraftCapes.getLogger().debug("Static cape loaded for {}", playerUUID);
        }
    }
    
    /**
     * Load the ears to the profile
     * @param earImage
     */
    public void applyEars(BufferedImage earImage) {
        final BufferedImage imgNew = new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics g = imgNew.getGraphics();
        g.drawImage(earImage, 24, 0, null);
        g.dispose();
        this.ears = applyTexture(imgNew);
        this.setHasEars(true);
    }

    /**
     * Sets the animated cape textures and loads all resources to memory
     * @param animatedCape
     */
    public void setAnimatedCape(Int2IntArrayMap animatedCape) {
        MinecraftCapes.getLogger().debug("Setting animated cape for {}", playerUUID);
        this.animatedCape = animatedCape;
        this.setHasStaticCape(false);
        this.setHasAnimatedCape(true);
    }
    
    /**
     * Gets the current frame for the player
     *
     * @return resourcelocation
     */
    private int getFrame() {
        final long time = System.currentTimeMillis();
        if(time > lastFrameTime + capeInterval) {
            int currentFrameNo = (lastFrame + 1 > animatedCape.size() - 1) ? 0 : lastFrame + 1;
            
            lastFrame = currentFrameNo;
            lastFrameTime = time;

            return animatedCape.get(currentFrameNo);
        }
        return animatedCape.get(lastFrame);
    }
    
    /**
     * Returns the player current cape resource
     * @return
     */
    public int getCapeLocation() {
        return hasStaticCape ? cape : hasAnimatedCape ? getFrame() : -1;
    }
    
    /**
     * Returns the players ear resource
     * @return
     */
    public int getEarLocation() {
        return hasEars ? ears : -1;
    }
    
    /**
     * Applys a texture on the render thread
     * @param bufferedImage
     */
    private int applyTexture(BufferedImage bufferedImage) {
        Minecraft gameInstance = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();
        return gameInstance.textureManager.load(bufferedImage);
    }
}