package net.minecraftcapes.player;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftcapes.MinecraftCapes;

import java.util.HashMap;
import java.util.UUID;

import static net.minecraftcapes.MinecraftCapes.MOD_ID;

public class PlayerHandler {

    private static HashMap<UUID, PlayerHandler> instances = new HashMap<UUID, PlayerHandler>();

    @Setter private boolean hasStaticCape = false;
    @Setter private boolean hasEars = false;
    @Setter private boolean hasAnimatedCape = false;
    @Getter @Setter private Boolean showCape = true;
    @Getter @Setter private Boolean hasCapeGlint = false;
    @Getter @Setter private boolean upsideDown = false;
    @Getter @Setter private Boolean hasInfo = false;
    @Setter @Getter private UUID playerUUID;

    @Getter
    private HashMap<Integer, NativeImage> animatedCape;

    //Animated Cape Settings
    private long lastFrameTime = 0;
    private int lastFrame = 0;
    private int capeInterval = 100;

    public PlayerHandler(UUID uuid) {
        this.playerUUID = uuid;
        PlayerHandler.instances.put(playerUUID, this);
    }

    @Deprecated
    public PlayerHandler(PlayerEntity player) {
        this.playerUUID = player.getUUID();
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
     * Tries to get the PlayerHandler instance from a player
     * @param player
     * @return
     */
    @Deprecated
    public static PlayerHandler getFromPlayer(PlayerEntity player) {
        return get(player.getUUID());
    }

    /**
     * Gets the cape texture and resizes or splits it accordingly
     * @param capeImage
     */
    public void applyCape(NativeImage capeImage) {
        //If the height is not 1/2 the width (32 == 64/2) then its an animated cape
        if(capeImage.getHeight() != capeImage.getWidth() / 2) {
            HashMap<Integer, NativeImage> animatedCape = new HashMap<Integer, NativeImage>();
            int totalFrames = capeImage.getHeight() / (capeImage.getWidth() / 2);
            for(int currentFrame = 0; currentFrame < totalFrames; currentFrame++) {
                NativeImage frame = new NativeImage(capeImage.getWidth(), capeImage.getWidth() / 2, true);
                for (int x = 0; x < frame.getWidth(); x++) {
                    for (int y = 0; y < frame.getHeight(); y++) {
                        frame.setPixelRGBA(x, y, capeImage.getPixelRGBA(x, y + (currentFrame * (capeImage.getWidth() / 2))));
                    }
                }
                animatedCape.put(currentFrame, frame);
            }
            setAnimatedCape(animatedCape);
            MinecraftCapes.getLogger().debug("Animated cape loaded for {}", playerUUID);
        } else {
            int imageWidth = 64;
            int imageHeight = 32;

            for (int srcWidth = capeImage.getWidth(), srcHeight = capeImage.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageWidth *= 2, imageHeight *= 2) {}

            final NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
            for (int x = 0; x < capeImage.getWidth(); x++) {
                for (int y = 0; y < capeImage.getHeight(); y++) {
                    imgNew.setPixelRGBA(x, y, capeImage.getPixelRGBA(x, y));
                }
            }

            applyTexture(new ResourceLocation(MOD_ID, "capes/" + playerUUID), imgNew);
            setHasStaticCape(true);
            MinecraftCapes.getLogger().debug("Static cape loaded for {}", playerUUID);
        }
    }

    public void applyEars(NativeImage earImage) {
        applyTexture(new ResourceLocation(MOD_ID, "ears/" + playerUUID), earImage);
        this.setHasEars(true);
    }

    /**
     * Unregister the cape
     */
    public void removeCape() {
        if(!hasStaticCape && !hasAnimatedCape) return;

        MinecraftCapes.getLogger().debug("Removing cape for {}", playerUUID);

        this.setHasAnimatedCape(false);
        this.setHasStaticCape(false);

        Minecraft.getInstance().execute(() -> {
            Minecraft.getInstance().getTextureManager().release(new ResourceLocation(MOD_ID, "capes/" + playerUUID));

            for (int i = 0; i < getAnimatedCape().size() - 1; i++) {
                Minecraft.getInstance().getTextureManager().release(new ResourceLocation(MOD_ID, String.format("capes/%s/%d", playerUUID, i)));
            }
        });
    }

    /**
     * Unregister the ears
     */
    public void removeEars() {
        if(!hasEars) return;

        MinecraftCapes.getLogger().debug("Removing ears for {}", playerUUID);

        this.setHasEars(false);
        Minecraft.getInstance().execute(() -> Minecraft.getInstance().getTextureManager().release(new ResourceLocation(MOD_ID, "ears/" + playerUUID)));
    }

    /**
     * Sets the animated cape textures and loads all resources to memory
     * @param animatedCape
     */
    public void setAnimatedCape(HashMap<Integer, NativeImage> animatedCape) {
        MinecraftCapes.getLogger().debug("Setting animated cape for {}", playerUUID);
        this.animatedCape = animatedCape;
        this.setHasAnimatedCape(true);
        this.loadFramesToResource();
    }

    /**
     * Load all NativeImages into a ResourceLocation
     */
    private void loadFramesToResource() {
        MinecraftCapes.getLogger().debug("Loading resources to memory for {}", playerUUID);
        for(final HashMap.Entry<Integer, NativeImage> entry : getAnimatedCape().entrySet()) {
            ResourceLocation currentResource = new ResourceLocation(MOD_ID, String.format("capes/%s/%d", playerUUID, entry.getKey()));
            applyTexture(currentResource, entry.getValue());
        }
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

            return new ResourceLocation(MOD_ID, String.format("capes/%s/%d", playerUUID, currentFrameNo));
        }
        return new ResourceLocation(MOD_ID, String.format("capes/%s/%d", playerUUID, lastFrame));
    }

    /**
     * Returns the player current cape resource
     * @return
     */
    public ResourceLocation getCapeLocation() {
        return hasStaticCape ? new ResourceLocation(MOD_ID, "capes/" + playerUUID) : hasAnimatedCape ? getFrame() : null;
    }

    /**
     * Returns the players ear resource
     * @return
     */
    public ResourceLocation getEarLocation() {
        return hasEars ? new ResourceLocation(MOD_ID, "ears/" + playerUUID) : null;
    }

    /**
     * Applys a texture on the render thread
     * @param resourceLocation
     * @param nativeImage
     */
    private void applyTexture(final ResourceLocation resourceLocation, final NativeImage nativeImage) {
        Minecraft.getInstance().execute(() -> Minecraft.getInstance().getTextureManager().register(resourceLocation, new DynamicTexture(nativeImage)));
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