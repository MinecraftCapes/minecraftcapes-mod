package co.uk.minecraftcapes.capabilities;

import co.uk.minecraftcapes.MinecraftCapes;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.UUID;

import static co.uk.minecraftcapes.reference.Reference.MODID;

public class PlayerHandler {

    @Setter private boolean hasStaticCape = false;
    @Setter private boolean hasEars = false;
    @Setter private boolean hasAnimatedCape = false;
    @Setter @Getter private Boolean hasInfo = false;
    @Setter @Getter private UUID playerUUID;

    @Getter
    private Int2ObjectMap<NativeImage> animatedCape;

    //Animated Cape Settings
    private long lastFrameTime = 0;
    private int lastFrame = 0;
    private int capeInterval = 100;

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
     * Load all NativeImages into a ResourceLocation
     */
    private void loadFramesToResource() {
        MinecraftCapes.getLogger().debug("Loading resources to memory for {}", playerUUID);
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

    /**
     * Storage for capabilities
     */
    public static class Storage implements Capability.IStorage {

        @Nullable
        @Override
        public INBT writeNBT(Capability capability, Object instance, Direction side) {
            return null;
        }

        @Override
        public void readNBT(Capability capability, Object instance, Direction side, INBT nbt) {

        }
    }
}
