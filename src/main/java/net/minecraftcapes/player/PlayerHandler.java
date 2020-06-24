package net.minecraftcapes.player;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraftcapes.MinecraftCapes;

import java.util.HashMap;
import java.util.UUID;

import static net.minecraftcapes.MinecraftCapes.MODID;

public class PlayerHandler {

    private static HashMap<UUID, PlayerHandler> instances = new HashMap<>();

    @Setter private boolean hasStaticCape = false;
    @Setter private boolean hasEars = false;
    @Setter private boolean hasAnimatedCape = false;
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
            MinecraftClient.getInstance().getTextureManager().registerTexture(currentResource, new NativeImageBackedTexture(nativeImage));
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
        if(hasStaticCape) {
            return new Identifier(MODID, "capes/" + playerUUID);
        } else if(hasAnimatedCape) {
            return getFrame();
        } else {
            return null;
        }
    }

    /**
     * Returns the players ear resource
     * @return
     */
    public Identifier getEarLocation() {
        Identifier Identifier = new Identifier(MODID, "ears/" + playerUUID);
        return hasEars ? Identifier : null;
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
                ", hasInfo=" + hasInfo +
                ", playerUUID=" + playerUUID +
                ", animatedCape=" + animatedCape +
                ", lastFrameTime=" + lastFrameTime +
                ", lastFrame=" + lastFrame +
                ", capeInterval=" + capeInterval +
                '}';
    }
}
