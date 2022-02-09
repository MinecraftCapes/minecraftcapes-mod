package net.minecraftcapes.gui;

import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.gui.widgets.OptionButton;
import net.minecraft.client.options.Option;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.events.PlayerEventHandler;
import net.minecraftcapes.player.PlayerHandler;

public class MenuScreen extends ScreenBase {

    public void init() {
        this.buttons.clear();
        int i = 0;

        //Custom Capes
        this.buttons.add(new OptionButton(i, this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), Option.INVERT_MOUSE, "Custom Capes" + getButtonText(MinecraftCapesConfig.isCapeVisible())));
        i++;

        //Custom Ears
        this.buttons.add(new OptionButton(i, this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), Option.INVERT_MOUSE, "Custom Ears" + getButtonText(MinecraftCapesConfig.isEarsVisible())));
        i++;

        //Force Reload Profile to get an extra line
        i++;

        //Reload Profile
        this.buttons.add(new Button(i, this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), "Reload Profile"));
        i++;

        //Done
        this.buttons.add(new Button(i, this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), "Done"));
    }

    private String getButtonText(boolean value) {
        return value ? ": ON" : ": OFF";
    }

    @Override
    protected void buttonClicked(Button button) {
        if(button.id == 0) {
            MinecraftCapesConfig.setCapeVisible(!MinecraftCapesConfig.isCapeVisible());
        }

        if(button.id == 1) {
            MinecraftCapesConfig.setEarsVisible(!MinecraftCapesConfig.isEarsVisible());
        }

        if(button.id == 3) {
            PlayerEventHandler.downloadProfile(PlayerHandler.getFromPlayer(this.minecraft.player));
        }

        if(button.id == 4) {
            this.minecraft.openScreen((ScreenBase)null);
            this.minecraft.lockCursor();
            return;
        }

        this.init();
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        this.drawTextWithShadowCentred(this.textManager, "MinecraftCapes Menu", this.width / 2, 20, 16777215);
        super.render(mouseX, mouseY, delta);
    }
}
