package net.minecraftcapes.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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
        int i = 0;

        //Custom Capes
        this.addButton(new Button(this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, getMessage(GuiOption.CAPE), (button) -> {
            MinecraftCapesConfig.setCapeVisible(!MinecraftCapesConfig.isCapeVisible());
            button.setMessage(getMessage(GuiOption.CAPE));
        }));
        i++;

        //Custom Ears
        this.addButton(new Button(this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, getMessage(GuiOption.EARS), (button -> {
            MinecraftCapesConfig.setEarsVisible(!MinecraftCapesConfig.isEarsVisible());
            button.setMessage(getMessage(GuiOption.EARS));
        })));
        i++;

        //Force Reload Profile to get an extra line
        i++;

        //Reload Profile
        this.addButton(new Button(this.width / 2 - 75, this.height / 6 + 24 * (i >> 1), 150, 20, "Reload Profile", (button -> {
            DownloadManager.prepareDownload(this.minecraft.player.getUUID(), this.minecraft.player.getName().getString(), true);
        })));
        i++;

        //Done
        this.addButton(new Button(this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), 200, 20, new TranslationTextComponent("gui.done").getColoredString(), (button) -> {
            this.minecraft.setScreen(null);
        }));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        this.drawCenteredString(this.font, this.title.getColoredString(), this.width / 2, 20, 16777215);
        super.render(mouseX, mouseY, partialTicks);
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