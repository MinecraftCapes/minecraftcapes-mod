package net.minecraftcapes.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;

public class MenuScreen extends Screen {

    private enum GuiOption {
        CAPE,
        EARS
    }

    public MenuScreen() {
        super(new TextComponent("MinecraftCapes"));
    }

    @Override
    public void init() {
        int xOffset = this.width / 6 * 4;
        int yOffset = this.height / 3;
        int i = 0;

        //Open MinecraftCapes
        this.addButton(new Button(xOffset - 75, yOffset, 150, 20, "Open MinecraftCapes", (button) -> {
            this.minecraft.setScreen(new ConfirmLinkScreen((openUrl) -> {
                if(openUrl) {
                    Util.getPlatform().openUri("https://minecraftcapes.net");
                }

                this.minecraft.setScreen(this);
            }, "https://minecraftcapes.net", true));
        }));
        i++;

        //For custom cape/ears onto new line
        i++;

        //Custom Capes
        this.addButton(new Button(xOffset - 155 + i % 2 * 160, yOffset + 24 * (i >> 1), 150, 20, getMessage(GuiOption.CAPE), (button) -> {
            MinecraftCapesConfig.setCapeVisible(!MinecraftCapesConfig.isCapeVisible());
            button.setMessage(getMessage(GuiOption.CAPE));
        }));
        i++;

        //Custom Ears
        this.addButton(new Button(xOffset - 155 + i % 2 * 160, yOffset + 24 * (i >> 1), 150, 20, getMessage(GuiOption.EARS), (button -> {
            MinecraftCapesConfig.setEarsVisible(!MinecraftCapesConfig.isEarsVisible());
            button.setMessage(getMessage(GuiOption.EARS));
        })));
        i++;

        //Force Reload Profile to get an extra line
        i++;

        //Reload Profile
        this.addButton(new Button(xOffset - 75, yOffset + 24 * (i >> 1), 150, 20, "Reload Profile", (button -> {
            DownloadManager.prepareDownload(this.minecraft.player.getUUID(), this.minecraft.player.getName().getString(), true);
        })));
        i++;

        //Done
        this.addButton(new Button(xOffset - 100, yOffset + 24 * (i >> 1), 200, 20, I18n.get("gui.done"), (button) -> {
            this.minecraft.setScreen(null);
        }));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        drawCenteredString(this.font, this.title.getContents(), this.width / 2, 20, 16777215);

        renderPlayer(
                this.width / 6,
                this.height / 3 + 90,
                60,
                this.minecraft.player
        );

        super.render(mouseX, mouseY, partialTicks);
    }

    private void renderPlayer(int x, int y, int scale, LivingEntity livingEntity) {
        RenderSystem.pushMatrix();
        RenderSystem.translatef(x, y, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        PoseStack poseStack = new PoseStack();
        poseStack.translate(0.0F, 0.0F, 1000.0F);
        poseStack.scale(scale, scale, scale);
        Quaternion var4 = Vector3f.ZP.rotationDegrees(180.0F);
        poseStack.mulPose(var4);
        float var6 = livingEntity.yBodyRot;
        float var7 = livingEntity.yRot;
        float var8 = livingEntity.xRot;
        float var9 = livingEntity.yHeadRotO;
        float var10 = livingEntity.yHeadRot;
        livingEntity.yBodyRot = 0.0F;
        livingEntity.yRot = 0.0F;
        livingEntity.xRot = 0.0F;
        livingEntity.yHeadRot = livingEntity.yRot;
        livingEntity.yHeadRotO = livingEntity.yRot;
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        entityRenderDispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        entityRenderDispatcher.render(livingEntity, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, poseStack, bufferSource, 15728880);
        bufferSource.endBatch();
        entityRenderDispatcher.setRenderShadow(true);
        livingEntity.yBodyRot = var6;
        livingEntity.yRot = var7;
        livingEntity.xRot = var8;
        livingEntity.yHeadRotO = var9;
        livingEntity.yHeadRot = var10;
        RenderSystem.popMatrix();
    }

    private String getMessage(GuiOption option) {
        String onOff;

        String buttonText = option.name();
        boolean value = false;
        if(option.equals(GuiOption.CAPE)) {
            buttonText = "Custom Capes";
            value = MinecraftCapesConfig.isCapeVisible();
        } else if(option.equals(GuiOption.EARS)) {
            buttonText = "Custom Ears";
            value = MinecraftCapesConfig.isEarsVisible();
        }

        if(value) {
            onOff = I18n.get("options.on");
        } else {
            onOff = I18n.get("options.off");
        }

        return buttonText + ": " + onOff;
    }
}