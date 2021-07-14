package net.minecraftcapes.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.events.PlayerEventHandler;
import net.minecraftcapes.player.PlayerHandler;

public class MenuScreen extends Screen {

    public MenuScreen() {
        super(new TranslationTextComponent("category.minecraftcapes.gui"));
    }

    protected void init() {
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
        this.addButton(new Button(this.width / 2 - 75, this.height / 6 + 24 * (i >> 1), 150, 20, ITextComponent.getTextComponentOrEmpty("Reload Profile"), (button) -> {
            PlayerEventHandler.downloadProfile(PlayerHandler.getFromPlayer(this.minecraft.player));
        }));
        i++;

        //Done
        this.addButton(new Button(this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), 200, 20, DialogTexts.GUI_DONE, (button) -> {
            this.minecraft.displayGuiScreen(null);
        }));
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 20, 16777215);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private ITextComponent getButtonString(String buttonText, boolean value) {
        return DialogTexts.getComposedOptionMessage(ITextComponent.getTextComponentOrEmpty(buttonText), value);
    }
}
