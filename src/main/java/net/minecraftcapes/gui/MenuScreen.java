package net.minecraftcapes.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.game.ConfirmChatLinkScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.platform.GLX;
import net.minecraft.client.render.platform.GlStateManager;
import net.minecraft.client.render.platform.Lighting;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.living.LivingEntity;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;

import java.net.URI;

public class MenuScreen extends Screen {

    private final String title = "MinecraftCapes";
    private final String url = "https://minecraftcapes.net";

    @Override
    public void init() {
        int xOffset = this.width / 6 * 4;
        int yOffset = this.height / 3;
        int i = 0;

        //Open MinecraftCapes
        this.buttons.add(new ButtonWidget(0,xOffset - 75, yOffset, 150, 20, "Open MinecraftCapes"));
        i++;

        //For custom cape/ears onto new line
        i++;

        //Custom Capes
        this.buttons.add(new ButtonWidget(1, xOffset - 155 + i % 2 * 160, yOffset + 24 * (i >> 1), 150, 20, getButtonString("Custom Capes", MinecraftCapesConfig.isCapeVisible())));
        i++;

        //Custom Ears
        this.buttons.add(new ButtonWidget(2, xOffset - 155 + i % 2 * 160, yOffset + 24 * (i >> 1), 150, 20, getButtonString("Custom Ears", MinecraftCapesConfig.isEarsVisible())));
        i++;

        //Force Reload Profile to get an extra line
        i++;

        //Reload Profile
        this.buttons.add(new ButtonWidget(3, xOffset - 75, yOffset + 24 * (i >> 1), 150, 20, "Reload Profile"));
        i++;

        //Done
        this.buttons.add(new ButtonWidget(4,xOffset - 100, yOffset + 24 * (i >> 1), 200, 20, I18n.translate("gui.done")));
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        if(button.active) {
            if(button.id == 0) {
                this.minecraft.openScreen(new ConfirmChatLinkScreen(this, url, 5, true));
            } else if(button.id == 1) {
                MinecraftCapesConfig.setCapeVisible(!MinecraftCapesConfig.isCapeVisible());
                button.message = getButtonString("Custom Capes", MinecraftCapesConfig.isCapeVisible());
            } else if(button.id == 2) {
                MinecraftCapesConfig.setEarsVisible(!MinecraftCapesConfig.isEarsVisible());
                button.message = getButtonString("Custom Ears", MinecraftCapesConfig.isEarsVisible());
            } else if(button.id == 3) {
                DownloadManager.prepareDownload(Minecraft.getInstance().player.getUuid(), Minecraft.getInstance().player.getName(), true);
            } else if(button.id == 4) {
                this.minecraft.openScreen(null);
            }
        }
    }

    @Override
    public void confirmResult(boolean result, int id) {
        if(id == 5) {
            if(result) {
                URI uri = URI.create(url);
                try {
                    Class<?> oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop").invoke(null);
                    oclass.getMethod("browse", URI.class).invoke(object, uri);
                } catch (Throwable throwable) {
                    MinecraftCapes.getLogger().error("Couldn't open link: {}", throwable.getMessage());
                }
            }

            this.minecraft.openScreen(null);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        this.drawCenteredString(this.textRenderer, this.title, this.width / 2, 20, 16777215);

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
        GlStateManager.scalef(-scale, scale, scale);
        GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
        float renderYawOffset = livingEntity.bodyYaw;
        float rotationYaw = livingEntity.yaw;
        float rotationPitch = livingEntity.pitch;
        float prevRotationYawHead = livingEntity.lastHeadYaw;
        float rotationYawHead = livingEntity.headYaw;
        Lighting.turnOn();
        livingEntity.bodyYaw = 0.0F;
        livingEntity.yaw = 0.0F;
        livingEntity.pitch = 0.0F;
        livingEntity.headYaw = livingEntity.yaw;
        livingEntity.lastHeadYaw = livingEntity.yaw;
        GlStateManager.translatef(0.0F, 0.0F, 0.0F);
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        entityRenderDispatcher.setCameraYaw(0.0F);
        entityRenderDispatcher.setRenderShadow(false);
        entityRenderDispatcher.render(livingEntity, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, false);
        entityRenderDispatcher.setRenderShadow(true);
        livingEntity.bodyYaw = renderYawOffset;
        livingEntity.yaw = rotationYaw;
        livingEntity.pitch = rotationPitch;
        livingEntity.lastHeadYaw = prevRotationYawHead;
        livingEntity.headYaw = rotationYawHead;
        GlStateManager.popMatrix();
        Lighting.turnOff();
        GlStateManager.disableRescaleNormal();
        GlStateManager.activeTexture(GLX.GL_TEXTURE1);
        GlStateManager.disableTexture();
        GlStateManager.activeTexture(GLX.GL_TEXTURE0);
    }

    private String getButtonString(String buttonText, boolean value) {
        String onOff;
        if(value) {
            onOff = I18n.translate("options.on");
        } else {
            onOff = I18n.translate("options.off");
        }

        return buttonText + ": " + onOff;
    }
}
