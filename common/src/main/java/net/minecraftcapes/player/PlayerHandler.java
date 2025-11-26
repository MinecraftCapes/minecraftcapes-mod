package net.minecraftcapes.player;

import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.config.MinecraftCapesConfig;

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
    
    @Getter
    private Int2ObjectMap<NativeImage> animatedCape;
    
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
    public void applyCape(NativeImage capeImage) {
        //If the height is not 1/2 the width (32 == 64/2) then its an animated cape
        if(capeImage.getHeight() != capeImage.getWidth() / 2) {
            Int2ObjectMap<NativeImage> animatedCapeFrames = new Int2ObjectOpenHashMap<>();
            int totalFrames = capeImage.getHeight() / (capeImage.getWidth() / 2);
            for(int currentFrame = 0; currentFrame < totalFrames; currentFrame++) {
                NativeImage frame = new NativeImage(capeImage.getWidth(), capeImage.getWidth() / 2, true);
                for (int x = 0; x < frame.getWidth(); x++) {
                    for (int y = 0; y < frame.getHeight(); y++) {
                        frame.setPixelRGBA(x, y, capeImage.getPixelRGBA(x, y + (currentFrame * (capeImage.getWidth() / 2))));
                    }
                }
                animatedCapeFrames.put(currentFrame, frame);
            }
            this.setAnimatedCape(animatedCapeFrames);
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
            
            capeImage.close();
            this.applyTexture(new ResourceLocation(MinecraftCapes.MOD_ID, "capes/" + playerUUID), imgNew);
            this.setHasStaticCape(true);
            this.setHasAnimatedCape(false);
            MinecraftCapes.getLogger().debug("Static cape loaded for {}", playerUUID);
        }
    }
    
    /**
     * Load the ears to the profile
     * @param earImage
     */
    public void applyEars(NativeImage earImage) {
        applyTexture(new ResourceLocation(MinecraftCapes.MOD_ID, "ears/" + playerUUID), earImage);
        this.setHasEars(true);
    }
    
    /**
     * Sets the animated cape textures and loads all resources to memory
     * @param animatedCape
     */
    public void setAnimatedCape(Int2ObjectMap<NativeImage> animatedCape) {
        MinecraftCapes.getLogger().debug("Setting animated cape for {}", playerUUID);
        this.animatedCape = animatedCape;
        this.setHasStaticCape(false);
        this.setHasAnimatedCape(true);
        this.loadFramesToResource();
    }
    
    /**
     * Load all NativeImages into a resourcelocation
     */
    private void loadFramesToResource() {
        MinecraftCapes.getLogger().debug("Loading resources to memory for {}", playerUUID);
        getAnimatedCape().forEach((integer, nativeImage) -> {
            ResourceLocation currentResource = new ResourceLocation(MinecraftCapes.MOD_ID, String.format("capes/%s/%d", playerUUID, integer));
            applyTexture(currentResource, nativeImage);
        });
    }
    
    /**
     * Gets the current frame for the player
     * @return resourcelocation
     */
    private ResourceLocation getFrame() {
        final long time = System.currentTimeMillis();
        if(time > lastFrameTime + capeInterval) {
            int currentFrameNo = (lastFrame + 1 > getAnimatedCape().size() - 1) ? 0 : lastFrame + 1;
            
            lastFrame = currentFrameNo;
            lastFrameTime = time;
            
            return new ResourceLocation(MinecraftCapes.MOD_ID, String.format("capes/%s/%d", playerUUID, currentFrameNo));
        }
        return new ResourceLocation(MinecraftCapes.MOD_ID, String.format("capes/%s/%d", playerUUID, lastFrame));
    }
    
    /**
     * Returns the player current cape resource
     * @return
     */
    public ResourceLocation getCapeLocation() {
        return hasStaticCape ? new ResourceLocation(MinecraftCapes.MOD_ID, "capes/" + playerUUID) : hasAnimatedCape ? getFrame() : null;
    }
    
    /**
     * Returns the players ear resource
     * @return
     */
    public ResourceLocation getEarLocation() {
        return hasEars ? new ResourceLocation(MinecraftCapes.MOD_ID, "ears/" + playerUUID) : null;
    }
    
    /**
     * Applys a texture on the render thread
     * @param resourcelocation
     * @param nativeImage
     */
    private void applyTexture(ResourceLocation resourcelocation, NativeImage nativeImage) {
        Minecraft.getInstance().execute(() -> Minecraft.getInstance().getTextureManager().register(resourcelocation, new DynamicTexture(nativeImage)));
    }
    
    /**
     * Create a player skin for with MinecraftCapes
     * @param original
     * @return
     */
    public PlayerSkin getSkin(PlayerSkin original) {
        //Set initial values
        ResourceLocation capeTexture = original.capeTexture();
        ResourceLocation elytraTexture = original.elytraTexture();
        
        //If we have a cape, lets load it
        if(MinecraftCapesConfig.isCapeVisible() && getCapeLocation() != null) {
            capeTexture = getCapeLocation();
            elytraTexture = capeTexture;
        }
        
        //Return new player skin
        return new PlayerSkin(
                original.texture(), original.textureUrl(),
                capeTexture, elytraTexture,
                original.model(), original.secure()
        );
    }
}