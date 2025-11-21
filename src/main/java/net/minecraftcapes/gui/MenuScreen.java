package net.minecraftcapes.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;

public class MenuScreen extends GuiScreen {

    private String title;

    @Override
    public void initGui() {
        //net.minecraft.client.gui.GuiCustomizeSkin;
        int i = 0;
        this.title = "MinecraftCapes";

        //Custom Capes
        this.buttonList.add(new GuiButton(0, this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, getButtonString("Custom Capes", MinecraftCapesConfig.isCapeVisible())));
        i++;

        //Custom Ears
        this.buttonList.add(new GuiButton(1, this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, getButtonString("Custom Ears", MinecraftCapesConfig.isEarsVisible())));
        i++;

        //Force Reload Profile to get an extra line
        i++;

        //Reload Profile
        this.buttonList.add(new GuiButton(2, this.width / 2 - 75, this.height / 6 + 24 * (i >> 1), 150, 20, "Reload Profile"));
        i++;

        //Done
        this.buttonList.add(new GuiButton(3,this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), 200, 20, I18n.format("gui.done")));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.enabled) {
            if(button.id == 0) {
                MinecraftCapesConfig.setCapeVisible(!MinecraftCapesConfig.isCapeVisible());
                button.displayString = getButtonString("Custom Capes", MinecraftCapesConfig.isCapeVisible());
            } else if(button.id == 1) {
                MinecraftCapesConfig.setEarsVisible(!MinecraftCapesConfig.isEarsVisible());
                button.displayString = getButtonString("Custom Ears", MinecraftCapesConfig.isEarsVisible());
            } else if(button.id == 2) {
                DownloadManager.prepareDownload(Minecraft.getMinecraft().player.getUniqueID(), Minecraft.getMinecraft().player.getName(), true);
            } else if(button.id == 3) {
                this.mc.displayGuiScreen(null);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
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
