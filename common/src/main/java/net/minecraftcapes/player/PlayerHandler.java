package net.minecraftcapes.player;

import lombok.Getter;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraftcapes.config.MinecraftCapesConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Client-thread profile state; texture ownership belongs to DownloadManager for the whole session. */
public class PlayerHandler {
    private static final Map<UUID, PlayerHandler> INSTANCES = new HashMap<>();
    /**
     * -- GETTER --
     * Returns the UUID used to identify this profile.
     */
    @Getter
    private final UUID uuid;
    /**
     * -- GETTER --
     * Returns the username used for non-v4 profile lookups.
     */
    @Getter
    private final String username;

    private boolean downloadStarted;
    private boolean capeGlint;
    private boolean upsideDown;
    private SharedTexture cape;
    private Identifier ears;
    private long lastFrameTime;
    private int lastFrame;
    private PlayerSkin originalSkin;
    private PlayerSkin customSkin;
    private ClientAsset.Texture skinCape;

    /** Creates a profile without allocating any textures. */
    private PlayerHandler(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    /** Returns the handler for a profile UUID, creating it on the client thread if necessary. */
    public static PlayerHandler get(UUID uuid, String username) {
        return INSTANCES.computeIfAbsent(uuid, key -> new PlayerHandler(key, username));
    }

    /** Removes profile state without releasing textures used by other players. */
    public static void remove(UUID uuid) {
        INSTANCES.remove(uuid);
    }

    /** Forces profile refreshes while retaining the session's shared textures. */
    public static void clearAll() {
        INSTANCES.clear();
    }

    /** Checks on the client thread whether a download still belongs to the active handler. */
    boolean isCurrent() {
        return INSTANCES.get(uuid) == this;
    }

    /** Marks the profile as attempted once; failed profiles wait for a manual refresh. */
    boolean beginDownload() {
        if (downloadStarted || !isCurrent()) return false;
        downloadStarted = true;
        return true;
    }

    /** Applies profile flags on the client thread if this handler has not been removed. */
    void applyProfile(boolean capeGlint, boolean upsideDown) {
        if (!isCurrent()) return;
        this.capeGlint = capeGlint;
        this.upsideDown = upsideDown;
    }

    /** References a registered shared cape without taking ownership of its GPU resources. */
    void applyCape(SharedTexture cape) {
        if (!isCurrent()) return;
        this.cape = cape;
        lastFrame = 0;
        lastFrameTime = 0;
        originalSkin = null;
        customSkin = null;
        skinCape = null;
    }

    /** References one static ear texture without taking ownership of its GPU resources. */
    void applyEars(Identifier ears) {
        if (isCurrent()) this.ears = ears;
    }

    /** Selects this player's animation frame using the existing 100 ms frame interval. */
    private ClientAsset.Texture getCapeTexture() {
        if (cape == null) return null;
        if (cape.frameCount() > 1) {
            long time = System.currentTimeMillis();
            if (time > lastFrameTime + 100) {
                lastFrame = (lastFrame + 1) % cape.frameCount();
                lastFrameTime = time;
            }
        }
        return cape.frame(lastFrame);
    }

    /** Returns the shared ear texture identifier, or null when the profile has no ears. */
    public Identifier getEarLocation() {
        return ears;
    }

    /** Reuses a skin wrapper until its original skin or selected shared cape frame changes. */
    public PlayerSkin getSkin(PlayerSkin original) {
        ClientAsset.Texture texture = MinecraftCapesConfig.isCapeVisible() ? getCapeTexture() : null;
        if (texture == null) return original;
        if ((originalSkin != original && customSkin != original) || skinCape != texture) {
            originalSkin = original;
            skinCape = texture;
            customSkin = new PlayerSkin(original.body(), texture, texture, original.model(), original.secure());
        }
        return customSkin;
    }

    /** Copies cosmetics into the avatar state while preserving the existing visibility rules. */
    public void extractRenderState(AvatarRenderState state) {
        if (!downloadStarted) return;
        ExtendedRenderState extended = (ExtendedRenderState) state;
        state.skin = getSkin(state.skin);
        state.isUpsideDown = upsideDown;
        state.showExtraEars = state.showExtraEars || (ears != null && MinecraftCapesConfig.isEarsVisible());
        extended.minecraftcapes$setCapeEnabled(MinecraftCapesConfig.isCapeVisible());
        extended.minecraftcapes$setGapeGlint(capeGlint);
        extended.minecraftcapes$setEarsEnabled(MinecraftCapesConfig.isEarsVisible());
        extended.minecraftcapes$setEarsTexture(getEarLocation());
    }
}
