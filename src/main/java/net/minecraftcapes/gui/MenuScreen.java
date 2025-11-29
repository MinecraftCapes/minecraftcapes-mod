package net.minecraftcapes.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Util;
import net.minecraftcapes.MinecraftCapes;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;

import java.net.URI;

public class MenuScreen extends GuiScreen {

    private final String title = "MinecraftCapes";
    private final String url = "https://minecraftcapes.net";

    @Override
    public void initGui() {
        int xOffset = this.width / 6 * 4;
        int yOffset = this.height / 3;
        int i = 0;

        //Open MinecraftCapes
        this.buttonList.add(new GuiButton(0,xOffset - 75, yOffset, 150, 20, "Open MinecraftCapes"));
        i++;

        //For custom cape/ears onto new line
        i++;

        //Custom Capes
        this.buttonList.add(new GuiButton(1, xOffset - 155 + i % 2 * 160, yOffset + 24 * (i >> 1), 150, 20, getButtonString("Custom Capes", MinecraftCapesConfig.isCapeVisible())));
        i++;

        //Custom Ears
        this.buttonList.add(new GuiButton(2, xOffset - 155 + i % 2 * 160, yOffset + 24 * (i >> 1), 150, 20, getButtonString("Custom Ears", MinecraftCapesConfig.isEarsVisible())));
        i++;

        //Force Reload Profile to get an extra line
        i++;

        //Reload Profile
        this.buttonList.add(new GuiButton(3, xOffset - 75, yOffset + 24 * (i >> 1), 150, 20, "Reload Profile"));
        i++;

        //Done
        this.buttonList.add(new GuiButton(4,xOffset - 100, yOffset + 24 * (i >> 1), 200, 20, I18n.format("gui.done")));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.enabled) {
            if(button.id == 0) {
                this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, url, 5, true));
            } else if(button.id == 1) {
                MinecraftCapesConfig.setCapeVisible(!MinecraftCapesConfig.isCapeVisible());
                button.displayString = getButtonString("Custom Capes", MinecraftCapesConfig.isCapeVisible());
            } else if(button.id == 2) {
                MinecraftCapesConfig.setEarsVisible(!MinecraftCapesConfig.isEarsVisible());
                button.displayString = getButtonString("Custom Ears", MinecraftCapesConfig.isEarsVisible());
            } else if(button.id == 3) {
                DownloadManager.prepareDownload(Minecraft.getMinecraft().thePlayer.getUniqueID(), Minecraft.getMinecraft().thePlayer.getName(), true);
            } else if(button.id == 4) {
                this.mc.displayGuiScreen(null);
            }
        }
    }

    @Override
    public void confirmClicked(boolean result, int id) {
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

            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 20, 16777215);

        renderPlayer(
                this.width / 6,
                this.height / 3 + 90,
                60,
                this.mc.thePlayer
        );

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void renderPlayer(int x, int y, int scale, EntityLivingBase livingEntity) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 50.0F);
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        float renderYawOffset = livingEntity.renderYawOffset;
        float rotationYaw = livingEntity.rotationYaw;
        float rotationPitch = livingEntity.rotationPitch;
        float prevRotationYawHead = livingEntity.prevRotationYawHead;
        float rotationYawHead = livingEntity.rotationYawHead;
        RenderHelper.enableStandardItemLighting();
        livingEntity.renderYawOffset = 0.0F;
        livingEntity.rotationYaw = 0.0F;
        livingEntity.rotationPitch = 0.0F;
        livingEntity.rotationYawHead = livingEntity.rotationYaw;
        livingEntity.prevRotationYawHead = livingEntity.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        renderManager.setPlayerViewY(180.0F);
        renderManager.setRenderShadow(false);
        renderManager.doRenderEntity(livingEntity, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, false);
        renderManager.setRenderShadow(true);
        livingEntity.renderYawOffset = renderYawOffset;
        livingEntity.rotationYaw = rotationYaw;
        livingEntity.rotationPitch = rotationPitch;
        livingEntity.prevRotationYawHead = prevRotationYawHead;
        livingEntity.rotationYawHead = rotationYawHead;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    private String getButtonString(String buttonText, boolean value) {
        String onOff;
        if(value) {
            onOff = I18n.format("options.on");
        } else {
            onOff = I18n.format("options.off");
        }

        return buttonText + ": " + onOff;
    }
}
