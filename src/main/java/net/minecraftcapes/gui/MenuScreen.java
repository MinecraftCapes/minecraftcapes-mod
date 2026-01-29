package net.minecraftcapes.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.locale.I18n;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.helpers.MinecraftApi;
import net.minecraftcapes.player.DownloadManager;

import java.util.UUID;

public class MenuScreen extends Screen {

    @Override
    public void init() {
        int i = 0;

        //Custom Capes
        this.buttons.add(
                new ButtonWidget(
                        i,
                        this.width / 2 - 155 + i % 2 * 160,
                        this.height / 6 + 24 * (i >> 1),
                        150,
                        20,
                        getButtonString("Custom Capes", MinecraftCapesConfig.isCapeVisible())
                )
        );
        i++;

        //Custom Ears
        this.buttons.add(
                new ButtonWidget(
                        i,
                        this.width / 2 - 155 + i % 2 * 160,
                        this.height / 6 + 24 * (i >> 1),
                        150,
                        20,
                        getButtonString("Custom Ears", MinecraftCapesConfig.isEarsVisible())
                )
        );
        i++;

        //Force Reload Profile to get an extra line
        i++;

        //Reload Profile
        this.buttons.add(
                new ButtonWidget(
                        i,
                        this.width / 2 - 75,
                        this.height / 6 + 24 * (i >> 1),
                        150,
                        20,
                        "Reload Profile"
                )
        );
        i++;

        //Done
        this.buttons.add(
                new ButtonWidget(
                        i,
                        this.width / 2 - 100,
                        this.height / 6 + 24 * (i >> 1),
                        200,
                        20,
                        I18n.translate("gui.done")
                )
        );
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        if(button.active) {
            if(button.id == 0) {
                MinecraftCapesConfig.setCapeVisible(!MinecraftCapesConfig.isCapeVisible());
                button.message = getButtonString("Custom Capes", MinecraftCapesConfig.isCapeVisible());
            } else if(button.id == 1) {
                MinecraftCapesConfig.setEarsVisible(!MinecraftCapesConfig.isEarsVisible());
                button.message = getButtonString("Custom Ears", MinecraftCapesConfig.isEarsVisible());
            } else if(button.id == 3) {
                button.active = false;
                new Thread(() -> {
                    UUID onlineUUID = MinecraftApi.getUUID(this.minecraft.player.name);
                    if(onlineUUID != null) {
                        DownloadManager.prepareDownload(onlineUUID, this.minecraft.player.name, true);
                        button.active = true;
                    }
                }).start();
            } else if(button.id == 4) {
                this.minecraft.openScreen(null);
            }
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        this.drawCenteredString(this.textRenderer, "MinecraftCApes", this.width / 2, 20, 16777215);
        super.render(mouseX, mouseY, partialTicks);
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
