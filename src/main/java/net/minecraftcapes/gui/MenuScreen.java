package net.minecraftcapes.gui;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConfirmOpenLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;

public class MenuScreen extends Screen {

    private enum GuiOption {
        CAPE,
        EARS
    }

    public MenuScreen() {
        super(new StringTextComponent("MinecraftCapes"));
    }

    @Override
    public void init() {
        int xOffset = this.width / 6 * 4;
        int yOffset = this.height / 3;
        int i = 0;

        //Open MinecraftCapes
        this.addButton(new Button(xOffset - 75, yOffset, 150, 20, "Open MinecraftCapes", (button) -> {
            this.minecraft.setScreen(new ConfirmOpenLinkScreen((openUrl) -> {
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
        this.drawCenteredString(this.font, this.title.getColoredString(), this.width / 2, 20, 16777215);

        renderPlayer(
                this.width / 6,
                this.height / 3 + 90,
                60,
                this.minecraft.player
        );

        super.render(mouseX, mouseY, partialTicks);
    }

    private void renderPlayer(int x, int y, int scale, LivingEntity livingEntity) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translatef(x, y, 50.0F);
        GlStateManager.scalef(scale, scale, scale);
        GlStateManager.rotatef(180.0F, 1.0F, 0.0F, 0.0F);
        float yBodyRot = livingEntity.yBodyRot;
        float yRot = livingEntity.yRot;
        float xRot = livingEntity.xRot;
        float yHeadRotO = livingEntity.yHeadRotO;
        float yHeadRot = livingEntity.yHeadRot;
        RenderHelper.turnOn();
        livingEntity.yBodyRot = 0.0F;
        livingEntity.yRot = 0.0F;
        livingEntity.xRot = 0.0F;
        livingEntity.yHeadRot = livingEntity.yRot;
        livingEntity.yHeadRotO = livingEntity.yRot;
        GlStateManager.translatef(0.0F, 0.0F, 0.0F);
        EntityRendererManager entityRendererManager = Minecraft.getInstance().getEntityRenderDispatcher();
        entityRendererManager.setPlayerRotY(180.0F);
        entityRendererManager.setRenderShadow(false);
        entityRendererManager.render(livingEntity, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, false);
        entityRendererManager.setRenderShadow(true);
        livingEntity.yBodyRot = yBodyRot;
        livingEntity.yRot = yRot;
        livingEntity.xRot = xRot;
        livingEntity.yHeadRotO = yHeadRotO;
        livingEntity.yHeadRot = yHeadRot;
        GlStateManager.popMatrix();
        RenderHelper.turnOff();
        GlStateManager.disableRescaleNormal();
        GlStateManager.activeTexture(GLX.GL_TEXTURE1);
        GlStateManager.disableTexture();
        GlStateManager.activeTexture(GLX.GL_TEXTURE0);
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
