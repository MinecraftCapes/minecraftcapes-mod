package net.minecraftcapes.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;

public class MenuScreen extends GuiScreen {

    private String title;

    @Override
    protected void initGui() {
        //net.minecraft.client.gui.GuiCustomizeSkin;
        int i = 0;
        this.title = I18n.format("category.minecraftcapes.gui");

        //Custom Capes
        this.addButton(new GuiButton(0, this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, getButtonString("Custom Capes", MinecraftCapesConfig.isCapeVisible())) {
            public void onClick ( double mouseX, double mouseY) {
                MinecraftCapesConfig.setCapeVisible(!MinecraftCapesConfig.isCapeVisible());
                this.displayString = getButtonString("Custom Capes", MinecraftCapesConfig.isCapeVisible());
            }
        });
        i++;

        //Custom Ears
        this.addButton(new GuiButton(1, this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, getButtonString("Custom Ears", MinecraftCapesConfig.isEarsVisible())) {
            public void onClick ( double mouseX, double mouseY) {
                MinecraftCapesConfig.setEarsVisible(!MinecraftCapesConfig.isEarsVisible());
                this.displayString = getButtonString("Custom Ears", MinecraftCapesConfig.isEarsVisible());
            }
        });
        i++;

        //Force Reload Profile to get an extra line
        i++;

        //Reload Profile
        this.addButton(new GuiButton(2, this.width / 2 - 75, this.height / 6 + 24 * (i >> 1), 150, 20, "Reload Profile") {
            public void onClick ( double mouseX, double mouseY) {
                DownloadManager.prepareDownload(MenuScreen.this.mc.player.getUniqueID(),true);
            }
        });
        i++;

        //Done
        this.addButton(new GuiButton(3,this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), 200, 20, I18n.format("gui.done")) {
            public void onClick(double mouseX, double mouseY) {
                MenuScreen.this.mc.displayGuiScreen(null);
            }
        });
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
        super.render(mouseX, mouseY, partialTicks);
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