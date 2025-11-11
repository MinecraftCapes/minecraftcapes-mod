package net.minecraftcapes.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;

public class MenuScreen extends Screen {
    
    public MenuScreen() {
        super(new TranslationTextComponent("category.minecraftcapes.gui"));
    }
    
    @Override
    public void init() {
        //net.minecraft.client.gui.GuiCustomizeSkin;
        int i = 0;

        //Custom Capes
        this.addButton(new Button(this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, getButtonString("Custom Capes", MinecraftCapesConfig.isCapeVisible()), (button) -> {
            MinecraftCapesConfig.setCapeVisible(!MinecraftCapesConfig.isCapeVisible());
        }));
        i++;

        //Custom Ears
        this.addButton(new Button(this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, getButtonString("Custom Ears", MinecraftCapesConfig.isEarsVisible()), (button -> {
            MinecraftCapesConfig.setEarsVisible(!MinecraftCapesConfig.isCapeVisible());
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
    public void render(int var1, int var2, float var3) {
        this.renderBackground();
        this.drawCenteredString(this.font, this.title.getColoredString(), this.width / 2, 20, 16777215);
        super.render(var1, var2, var3);
    }


    private String getButtonString(String buttonText, boolean value) {
        String onOff;
        if (value) {
            onOff = I18n.get("options.on");
        } else {
            onOff = I18n.get("options.off");
        }
        
        return buttonText + ": " + onOff;
    }
}
