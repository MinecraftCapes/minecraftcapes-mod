package net.minecraftcapes.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.events.PlayerEventHandler;
import net.minecraftcapes.player.PlayerHandler;

public class MenuScreen extends Screen {

    public MenuScreen() {
        super(new TranslationTextComponent("category.minecraftcapes.gui"));
    }

    @Override
    protected void init() {
        //net.minecraft.client.gui.screen.CustomizeSkinScreen;
        int i = 0;

        //Custom Capes
        this.addButton(new Button(this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, getButtonString("Custom Capes", MinecraftCapesConfig.isCapeVisible()), (button) -> {
            MinecraftCapesConfig.setCapeVisible(!MinecraftCapesConfig.isCapeVisible());
            button.setMessage(getButtonString("Custom Capes", MinecraftCapesConfig.isCapeVisible()));
        }));
        i++;

        //Custom Ears
        this.addButton(new Button(this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, getButtonString("Custom Ears", MinecraftCapesConfig.isEarsVisible()), (button) -> {
            MinecraftCapesConfig.setEarsVisible(!MinecraftCapesConfig.isEarsVisible());
            button.setMessage(getButtonString("Custom Ears", MinecraftCapesConfig.isEarsVisible()));
        }));
        i++;

        //Force Reload Profile to get an extra line
        i++;

        //Reload Profile
        this.addButton(new Button(this.width / 2 - 75, this.height / 6 + 24 * (i >> 1), 150, 20, "Reload Profile", (button) -> {
            PlayerEventHandler.downloadProfile(PlayerHandler.getFromPlayer(this.minecraft.player));
        }));
        i++;

        //Done
        this.addButton(new Button(this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), 200, 20, I18n.format("gui.done"), (button) -> {
            this.minecraft.displayGuiScreen(null);
        }));
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground();
        this.drawCenteredString(this.font, this.title.getFormattedText(), this.width / 2, 20, 16777215);
        super.render(p_render_1_, p_render_2_, p_render_3_);
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
