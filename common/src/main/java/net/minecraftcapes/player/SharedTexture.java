package net.minecraftcapes.player;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ExternalTextureProvider;
import net.minecraft.client.renderer.texture.TextureResources;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraftcapes.MinecraftCapes;

import java.util.ArrayList;
import java.util.List;

/** Immutable frame references shared by all profiles using one texture hash. */
final class SharedTexture implements AutoCloseable {
    private final ClientAsset.Texture[] frames;
    private final List<TextureResources> resources;

    /** Retains frame identifiers and the GPU resources that must be closed at game shutdown. */
    private SharedTexture(ClientAsset.Texture[] frames, List<TextureResources> resources) {
        this.frames = frames;
        this.resources = resources;
    }

    /** Returns the number of uploaded frames, including one for static textures. */
    int frameCount() {
        return frames.length;
    }

    /** Returns a reusable texture reference for a frame selected by the player handler. */
    ClientAsset.Texture frame(int index) {
        return frames[index];
    }

    /** Returns the single static texture location, rejecting animated resources. */
    Identifier staticLocation() {
        if (frames.length != 1) throw new IllegalStateException("Expected a static texture");
        return frames[0].texturePath();
    }

    /** Unregisters and closes this texture on the client thread, only at shutdown or failed upload. */
    @Override
    public void close() {
        for (ClientAsset.Texture frame : frames) {
            if (frame != null) Minecraft.getInstance().getTextureManager().release(frame.texturePath());
        }
        for (TextureResources resource : resources) resource.close();
        resources.clear();
    }

    /** Keeps the entire ear image as one static texture, without splitting or resizing it. */
    static Prepared prepareEars(NativeImage image) {
        List<NativeImage> images = new ArrayList<>(1);
        images.add(image);
        return new Prepared(images, false);
    }

    /** Splits an animated cape or pads a static cape, always consuming the supplied image. */
    static Prepared prepareCape(NativeImage image) {
        List<NativeImage> frames = new ArrayList<>();
        boolean retained = false;
        try {
            int width = image.getWidth();
            int height = image.getHeight();
            int frameHeight = width / 2;
            if (frameHeight == 0 || height < frameHeight) {
                throw new IllegalArgumentException("Invalid cape dimensions: " + width + "x" + height);
            }
            boolean animated = height != frameHeight;
            if (animated) {
                int count = height / frameHeight;
                for (int i = 0; i < count; i++) {
                    NativeImage frame = new NativeImage(width, frameHeight, true);
                    frames.add(frame);
                    copy(image, frame, i * frameHeight, width, frameHeight);
                }
            } else {
                int targetWidth = 64;
                int targetHeight = 32;
                while (targetWidth < width || targetHeight < height) {
                    targetWidth *= 2;
                    targetHeight *= 2;
                }
                if (targetWidth == width && targetHeight == height) {
                    frames.add(image);
                    retained = true;
                } else {
                    NativeImage frame = new NativeImage(targetWidth, targetHeight, true);
                    frames.add(frame);
                    copy(image, frame, 0, width, height);
                }
            }
            return new Prepared(frames, animated);
        } catch (RuntimeException | Error e) {
            for (NativeImage frame : frames) frame.close();
            throw e;
        } finally {
            if (!retained) image.close();
        }
    }

    /** Copies a horizontal image region in row order without changing the existing pixel layout. */
    private static void copy(NativeImage source, NativeImage target, int offsetY, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) target.setPixel(x, y, source.getPixel(x, y + offsetY));
        }
    }

    /** Owns decoded frames until the client thread uploads them or shutdown discards them. */
    static final class Prepared implements AutoCloseable {
        private final List<NativeImage> images;
        private final boolean animated;

        /** Takes ownership of the prepared native images. */
        private Prepared(List<NativeImage> images, boolean animated) {
            this.images = images;
            this.animated = animated;
        }

        /** Uploads all frames once on the client thread and closes their CPU-side images. */
        SharedTexture register(String key) {
            ClientAsset.Texture[] frames = new ClientAsset.Texture[images.size()];
            List<TextureResources> resources = new ArrayList<>(images.size());
            SharedTexture texture = new SharedTexture(frames, resources);
            try {
                for (int i = 0; i < images.size(); i++) {
                    Identifier id = Identifier.fromNamespaceAndPath(MinecraftCapes.MOD_ID, animated ? key + "/" + i : key);
                    ClientAsset.Texture frame = new ClientAsset.DownloadedTexture(id, "minecraftcapes");
                    TextureResources resource = TextureResources.from2dImage(frame.texturePath()::toString, images.get(i));
                    resources.add(resource);
                    frames[i] = frame;
                    Minecraft.getInstance().getTextureManager().registerProvider(frame.texturePath(), new ExternalTextureProvider(resource));
                }
                return texture;
            } catch (RuntimeException | Error e) {
                texture.close();
                throw e;
            } finally {
                close();
            }
        }

        /** Releases decoded frames that have either been uploaded or abandoned. */
        @Override
        public void close() {
            for (NativeImage image : images) image.close();
            images.clear();
        }
    }
}
