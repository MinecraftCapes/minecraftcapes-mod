package co.uk.minecraftcapes.helpers;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.UUID;

import static co.uk.minecraftcapes.reference.Reference.MODID;

public class PlayerHandler {

    private UUID playerUUID;

    @Setter private boolean hasStaticCape = false;
    @Setter private boolean hasEars = false;
    @Setter private boolean hasAnimatedCape = false;
    @Getter @Setter private Boolean hasInfo = false;

    @Getter
    private Int2ObjectMap<NativeImage> animatedCape;

    //Animated Cape Settings
    private long lastFrameTime = 0;
    private int lastFrame = 0;
    private int capeInterval = 100;

    //Instances
    private static HashMap<UUID, PlayerHandler> instances = new HashMap<UUID, PlayerHandler>();

    /**
     * Loads a new instance in the collection
     * @param playerUUID Player UUID
     */
    public PlayerHandler(UUID playerUUID) {
        this.playerUUID = playerUUID;
        instances.put(playerUUID, this);
    }

    /**
     * Get an instance statically from the Players UUID
     * @param uuid
     * @return PlayerHandler
     */
    public static PlayerHandler getPlayer(UUID uuid) {
        return (instances.containsKey(uuid)) ? instances.get(uuid) : new PlayerHandler(uuid);
    }

    /**
     * Sets the animated cape textures and loads all resources to memory
     * @param animatedCape
     */
    public void setAnimatedCape(Int2ObjectMap<NativeImage> animatedCape) {
        this.animatedCape = animatedCape;
        this.setHasAnimatedCape(true);
        this.loadFramesToResource();
    }

    /**
     * Load all NativeImages into a ResourceLocation
     */
    private void loadFramesToResource() {
        getAnimatedCape().forEach((integer, nativeImage) -> {
            ResourceLocation currentResource = new ResourceLocation(MODID, String.format("capes/%s/%d", playerUUID, integer));
            Minecraft.getInstance().getTextureManager().loadTexture(currentResource, new DynamicTexture(nativeImage));
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
        if(hasStaticCape) {
            return new ResourceLocation(MODID, "capes/" + playerUUID);
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
    public ResourceLocation getEarLocation() {
        ResourceLocation resourceLocation = new ResourceLocation(MODID, "ears/" + playerUUID);
        return hasEars ? resourceLocation : null;
    }
}
