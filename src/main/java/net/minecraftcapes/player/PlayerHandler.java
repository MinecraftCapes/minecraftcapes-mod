package net.minecraftcapes.player;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraftcapes.MinecraftCapes;
import org.apache.commons.codec.binary.Base64;

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
    @Getter @Setter private Boolean forceShowElytra = false;
    @Getter @Setter private Boolean hasCapeGlint = false;
    @Getter @Setter private boolean upsideDown = false;
    @Getter @Setter private Boolean hasInfo = false;
    @Setter @Getter private UUID playerUUID;

    @Getter
    private Int2ObjectMap<NativeImage> animatedCape;

    //Animated Cape Settings
    private long lastFrameTime = 0;
    private int lastFrame = 0;
    private int capeInterval = 100;

    public PlayerHandler(PlayerEntity player) {
        this.playerUUID = player.getUuid();
        PlayerHandler.instances.put(playerUUID, this);
    }

    /**
     * Tries to get the PlayerHandler instance from a player
     * @param player
     * @return
     */
    public static PlayerHandler getFromPlayer(PlayerEntity player) {
        PlayerHandler playerHandler = PlayerHandler.instances.get(player.getUuid());
        return playerHandler == null ? new PlayerHandler(player) : playerHandler;
    }

    /**
     * Reads a base64 string and converts it to a NativeImage
     * @param textureBase64
     * @return
     */
    private NativeImage readTexture(String textureBase64) {
        try {
            byte[] imgBytes = Base64.decodeBase64(textureBase64);
            ByteArrayInputStream bias = new ByteArrayInputStream(imgBytes);
            return NativeImage.read(bias);
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
        NativeImage capeImage = readTexture(cape);
        //If the height is not 1/2 the width (32 == 64/2) then its an animated cape
        if(capeImage.getHeight() != capeImage.getWidth() / 2) {
            Int2ObjectMap<NativeImage> animatedCape = new Int2ObjectOpenHashMap<>();
            int totalFrames = capeImage.getHeight() / (capeImage.getWidth() / 2);
            for(int currentFrame = 0; currentFrame < totalFrames; currentFrame++) {
                NativeImage frame = new NativeImage(capeImage.getWidth(), capeImage.getWidth() / 2, true);
                for (int x = 0; x < frame.getWidth(); x++) {
                    for (int y = 0; y < frame.getHeight(); y++) {
                        frame.setPixelColor(x, y, capeImage.getPixelColor(x, y + (currentFrame * (capeImage.getWidth() / 2))));
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
                    imgNew.setPixelColor(x, y, capeImage.getPixelColor(x, y));
                }
            }

            capeImage.close();
            applyTexture(new Identifier(MODID, "capes/" + playerUUID), imgNew);
            setHasStaticCape(true);
            MinecraftCapes.getLogger().debug("Static cape loaded for {}", playerUUID);
        }
    }

    public void applyEars(String ears) {
        NativeImage earImage = readTexture(ears);
        applyTexture(new Identifier(MODID, "ears/" + playerUUID), earImage);
        this.setHasEars(true);
    }

    /**
     * Sets the animated cape textures and loads all resources to memory
     * @param animatedCape
     */
    public void setAnimatedCape(Int2ObjectMap<NativeImage> animatedCape) {
        MinecraftCapes.getLogger().debug("Setting animated cape for {}", playerUUID);
        this.animatedCape = animatedCape;
        this.setHasAnimatedCape(true);
        this.loadFramesToResource();
    }

    /**
     * Load all NativeImages into a Identifier
     */
    private void loadFramesToResource() {
        MinecraftCapes.getLogger().debug("Loading resources to memory for {}", playerUUID);
        getAnimatedCape().forEach((integer, nativeImage) -> {
            Identifier currentResource = new Identifier(MODID, String.format("capes/%s/%d", playerUUID, integer));
            applyTexture(currentResource, nativeImage);
        });
    }

    /**
     * Gets the current frame for the player
     * @return Identifier
     */
    private Identifier getFrame() {
        final long time = System.currentTimeMillis();
        if(time > lastFrameTime + capeInterval) {
            int currentFrameNo = (lastFrame + 1 > getAnimatedCape().size() - 1) ? 0 : lastFrame + 1;

            lastFrame = currentFrameNo;
            lastFrameTime = time;

            return new Identifier(MODID, String.format("capes/%s/%d", playerUUID, currentFrameNo));
        }
        return new Identifier(MODID, String.format("capes/%s/%d", playerUUID, lastFrame));
    }

    /**
     * Returns the player current cape resource
     * @return
     */
    public Identifier getCapeLocation() {
        return hasStaticCape ? new Identifier(MODID, "capes/" + playerUUID) : hasAnimatedCape ? getFrame() : null;
    }

    /**
     * Returns the players ear resource
     * @return
     */
    public Identifier getEarLocation() {
        return hasEars ? new Identifier(MODID, "ears/" + playerUUID) : null;
    }

    /**
     * Applys a texture on the render thread
     * @param identifier
     * @param nativeImage
     */
    private void applyTexture(Identifier identifier, NativeImage nativeImage) {
        MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, new NativeImageBackedTexture(nativeImage)));
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
