package net.minecraftcapes.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.events.PlayerEventHandler;
import net.minecraftcapes.player.PlayerHandler;

public class MenuScreen extends Screen {

    public MenuScreen(Text title) {
        super(title);
    }

    protected void init() {
        //net.minecraft.client.gui.screen.options.SkinOptionsScreen;
        int i = 0;

        //Custom Capes
        this.addButton(new ButtonWidget(this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, getButtonString("Custom Capes", MinecraftCapesConfig.isCapeVisible()), (button) -> {
            MinecraftCapesConfig.setCapeVisible(!MinecraftCapesConfig.isCapeVisible());
            button.setMessage(getButtonString("Custom Capes", MinecraftCapesConfig.isCapeVisible()));
        }));
        i++;

        //Custom Ears
        this.addButton(new ButtonWidget(this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, getButtonString("Custom Ears", MinecraftCapesConfig.isEarsVisible()), (button) -> {
            MinecraftCapesConfig.setEarsVisible(!MinecraftCapesConfig.isEarsVisible());
            button.setMessage(getButtonString("Custom Ears", MinecraftCapesConfig.isEarsVisible()));
        }));
        i++;

        //Force Reload Profile to get an extra line
        i++;

        //Reload Profile
        this.addButton(new ButtonWidget(this.width / 2 - 75, this.height / 6 + 24 * (i >> 1), 150, 20, "Reload Profile", (button) -> {
            PlayerEventHandler.downloadProfile(PlayerHandler.getFromPlayer(this.minecraft.player));
        }));
        i++;

        //Done
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), 200, 20, I18n.translate("gui.done", new Object[0]), (button) -> {
            this.minecraft.openScreen(null);
        }));
    }

    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        this.drawCenteredString(this.font, this.title.asFormattedString(), this.width / 2, 20, 16777215);
        super.render(mouseX, mouseY, delta);
    }

    private String getButtonString(String buttonText, boolean value) {
        String onOff;
        if(value) {
            onOff = I18n.translate("options.on", new Object[0]);
        } else {
            onOff = I18n.translate("options.off", new Object[0]);
        }
        return buttonText + ": " + onOff;
    }
}
