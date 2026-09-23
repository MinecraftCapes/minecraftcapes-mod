package net.minecraftcapes.player;

import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.config.MinecraftCapesConfig;

import java.util.HashMap;
import java.util.UUID;

public class PlayerHandler {

    private static final HashMap<UUID, PlayerHandler> instances = new HashMap<>();

    @Setter private boolean hasStaticCape = false;
    @Setter @Getter private boolean hasEars = false;
    @Setter private boolean hasAnimatedCape = false;
    @Getter @Setter private Boolean hasCapeGlint = false;
    @Getter @Setter private boolean upsideDown = false;
    @Getter @Setter private Boolean hasInfo = false;
    @Getter private final UUID uuid;
    @Setter @Getter private String name;

    @Getter
    private Int2ObjectMap<NativeImage> animatedCape;

    //Animated Cape Settings
    private long lastFrameTime = 0;
    private int lastFrame = 0;
    
    public PlayerHandler(UUID uuid) {
        this.uuid = uuid;
        PlayerHandler.instances.put(uuid, this);
    }

    /**
     * Tries to get the PlayerHandler instance from a player
     * @param uuid The player profile UUID
     * @return The player handler
     */
    public static PlayerHandler get(UUID uuid) {
        PlayerHandler playerHandler = PlayerHandler.instances.get(uuid);
        return playerHandler == null ? new PlayerHandler(uuid) : playerHandler;
    }

    /**
     * Remove a player
     * @param uuid The player profile UUID
     */
    public static void remove(UUID uuid) {
        PlayerHandler playerHandler = instances.get(uuid);
        playerHandler.removeCape();
        playerHandler.removeEars();
        instances.remove(uuid);
    }
    
    /**
     * Remove all instances to force a refresh
     */
    public static void clearAll() {
        for(PlayerHandler playerHandler : instances.values()) {
            playerHandler.removeCape();
            playerHandler.removeEars();
        }
        
        instances.clear();
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
                        frame.setPixel(x, y, capeImage.getPixel(x, y + (currentFrame * (capeImage.getWidth() / 2))));
                    }
                }
                animatedCapeFrames.put(currentFrame, frame);
            }
            this.setAnimatedCape(animatedCapeFrames);
            MinecraftCapes.getLogger().debug("Animated cape loaded for {}", uuid);
        } else {
            int imageWidth = 64;
            int imageHeight = 32;

            for (int srcWidth = capeImage.getWidth(), srcHeight = capeImage.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageWidth *= 2, imageHeight *= 2) {}

            final NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
            for (int x = 0; x < capeImage.getWidth(); x++) {
                for (int y = 0; y < capeImage.getHeight(); y++) {
                    imgNew.setPixel(x, y, capeImage.getPixel(x, y));
                }
            }

            capeImage.close();
            this.applyTexture(Identifier.fromNamespaceAndPath(MinecraftCapes.MOD_ID, "capes/" + uuid), imgNew);
            this.setHasStaticCape(true);
            this.setHasAnimatedCape(false);
            MinecraftCapes.getLogger().debug("Static cape loaded for {}", uuid);
        }
    }

    /**
     * Load the ears to the profile
     * @param earImage
     */
    public void applyEars(NativeImage earImage) {
        applyTexture(Identifier.fromNamespaceAndPath(MinecraftCapes.MOD_ID, "ears/" + uuid), earImage);
        this.setHasEars(true);
    }

    /**
     * Unregister the cape
     */
    public void removeCape() {
        if(!hasStaticCape && !hasAnimatedCape) return;

        MinecraftCapes.getLogger().debug("Removing cape for {}", uuid);

        this.setHasAnimatedCape(false);
        this.setHasStaticCape(false);

        Minecraft.getInstance().execute(() -> {
            Minecraft.getInstance().getTextureManager().release(Identifier.fromNamespaceAndPath(MinecraftCapes.MOD_ID, "capes/" + uuid));

            if(animatedCape != null) {
                for(int i = 0; i < animatedCape.size() - 1; i++) {
                    Minecraft.getInstance()
                            .getTextureManager()
                            .release(Identifier.fromNamespaceAndPath(MinecraftCapes.MOD_ID, String.format("capes/%s/%d", uuid, i)));
                }
            }
        });
    }

    /**
     * Unregister the ears
     */
    public void removeEars() {
        if(!hasEars) return;

        MinecraftCapes.getLogger().debug("Removing ears for {}", uuid);

        this.setHasEars(false);
        Minecraft.getInstance().execute(() -> Minecraft.getInstance().getTextureManager().release(Identifier.fromNamespaceAndPath(MinecraftCapes.MOD_ID, "ears/" + uuid)));
    }

    /**
     * Sets the animated cape textures and loads all resources to memory
     * @param animatedCape
     */
    public void setAnimatedCape(Int2ObjectMap<NativeImage> animatedCape) {
        MinecraftCapes.getLogger().debug("Setting animated cape for {}", uuid);
        this.animatedCape = animatedCape;
        this.setHasStaticCape(false);
        this.setHasAnimatedCape(true);
        this.loadFramesToResource();
    }

    /**
     * Load all NativeImages into a Identifier
     */
    private void loadFramesToResource() {
        MinecraftCapes.getLogger().debug("Loading resources to memory for {}", uuid);
        animatedCape.forEach((integer, nativeImage) -> {
            Identifier currentResource = Identifier.fromNamespaceAndPath(MinecraftCapes.MOD_ID, String.format("capes/%s/%d", uuid, integer));
            applyTexture(currentResource, nativeImage);
        });
    }

    /**
     * Gets the current frame for the player
     * @return Identifier
     */
    private Identifier getFrame() {
        final long time = System.currentTimeMillis();
        int capeInterval = 100;
        if(time > lastFrameTime + capeInterval) {
            int currentFrameNo = (lastFrame + 1 > getAnimatedCape().size() - 1) ? 0 : lastFrame + 1;

            lastFrame = currentFrameNo;
            lastFrameTime = time;

            return Identifier.fromNamespaceAndPath(MinecraftCapes.MOD_ID, String.format("capes/%s/%d", uuid, currentFrameNo));
        }
        return Identifier.fromNamespaceAndPath(MinecraftCapes.MOD_ID, String.format("capes/%s/%d", uuid, lastFrame));
    }

    /**
     * Returns the player current cape resource
     * @return
     */
    public Identifier getCapeLocation() {
        return hasStaticCape ? Identifier.fromNamespaceAndPath(MinecraftCapes.MOD_ID, "capes/" + uuid) : hasAnimatedCape ? getFrame() : null;
    }

    /**
     * Returns the players ear resource
     * @return
     */
    public Identifier getEarLocation() {
        return hasEars ? Identifier.fromNamespaceAndPath(MinecraftCapes.MOD_ID, "ears/" + uuid) : null;
    }

    /**
     * Applys a texture on the render thread
     * @param Identifier
     * @param nativeImage
     */
    private void applyTexture(Identifier Identifier, NativeImage nativeImage) {
        Minecraft.getInstance().execute(() -> Minecraft.getInstance().getTextureManager().register(Identifier, new DynamicTexture(Identifier::toString, nativeImage)));
    }

    /**
     * Create a player skin for with MinecraftCapes
     * @param original
     * @return
     */
    public PlayerSkin getSkin(PlayerSkin original) {
        //Set initial values
        ClientAsset.Texture capeTexture = original.cape();
        ClientAsset.Texture elytraTexture = original.elytra();

        //If we have a cape, lets load it
        if(MinecraftCapesConfig.isCapeVisible() && getCapeLocation() != null) {
            capeTexture = new ClientAsset.ResourceTexture(getCapeLocation(), getCapeLocation());
            elytraTexture = capeTexture;
        }

        //Return new player skin
        return new PlayerSkin(
                original.body(),
                capeTexture, elytraTexture,
                original.model(), original.secure()
        );
    }
}