package co.uk.minecraftcapes.helpers;

import co.uk.minecraftcapes.events.PlayerEventHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.UUID;

public class AnimatedCapeHandler {

    private UUID playerUUID;
    private long lastFrameTime = 0;
    private int lastFrame = 0;
    private int capeInterval = 100;

    //Instances
    private static HashMap<UUID, AnimatedCapeHandler> instances = new HashMap<UUID, AnimatedCapeHandler>();

    public AnimatedCapeHandler(UUID playerUUID) {
        this.playerUUID = playerUUID;
        instances.put(playerUUID, this);
    }

    public static AnimatedCapeHandler getPlayer(UUID uuid) {
        return (instances.containsKey(uuid)) ? instances.get(uuid) : new AnimatedCapeHandler(uuid);
    }

    public ResourceLocation getFrame() {
        final long time = System.currentTimeMillis();
        if(time > lastFrameTime + capeInterval) {
            Int2ObjectMap<NativeImage> animatedCape = PlayerEventHandler.getAnimatedCape(playerUUID);
            int currentFrameNo = (lastFrame + 1 > animatedCape.size() - 1) ? 0 : lastFrame + 1;

            lastFrame = currentFrameNo;
            lastFrameTime = time;

            ResourceLocation currentResource = new ResourceLocation(String.format("capes/%s/%d", playerUUID, currentFrameNo));
            Minecraft.getInstance().getTextureManager().loadTexture(currentResource, new DynamicTexture(animatedCape.get(currentFrameNo)));
            return currentResource;
        }
        return new ResourceLocation(String.format("capes/%s/%d", playerUUID, lastFrame));
    }
}
