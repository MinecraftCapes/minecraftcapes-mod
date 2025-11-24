package net.minecraftcapes.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraftcapes.config.MinecraftCapesConfig;

public class MenuScreen extends Screen {

    @Override
    public void init() {
        //net.minecraft.client.gui.GuiCustomizeSkin;
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
                        TranslationStorage.getInstance().get("gui.done")
                )
        );
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        if(button.active) {
            if(button.id == 0) {
                MinecraftCapesConfig.setCapeVisible(!MinecraftCapesConfig.isCapeVisible());
                button.text = getButtonString("Custom Capes", MinecraftCapesConfig.isCapeVisible());
            } else if(button.id == 1) {
                MinecraftCapesConfig.setEarsVisible(!MinecraftCapesConfig.isEarsVisible());
                button.text = getButtonString("Custom Ears", MinecraftCapesConfig.isEarsVisible());
            } else if(button.id == 2) {
                //DownloadManager.prepareDownload(Minecraft.getMinecraft().thePlayer.getUniqueID(), Minecraft.getMinecraft().thePlayer.getDisplayName(), true);
            } else if(button.id == 3) {
                this.minecraft.setScreen(null);
            }
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        this.drawCenteredTextWithShadow(this.textRenderer, "MinecraftCApes", this.width / 2, 20, 16777215);
        super.render(mouseX, mouseY, partialTicks);
    }

    private String getButtonString(String buttonText, boolean value) {
        String onOff;
        if(value) {
            onOff = TranslationStorage.getInstance().get("options.on");
        } else {
            onOff = TranslationStorage.getInstance().get("options.off");
        }

        return buttonText + ": " + onOff;
    }
}
