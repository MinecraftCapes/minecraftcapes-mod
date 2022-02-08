package net.minecraftcapes.gui;

import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.events.PlayerEventHandler;
import net.minecraftcapes.player.PlayerHandler;

public class MenuScreen extends ScreenBase {

//    protected void init() {
//        //net.minecraft.client.gui.screen.option.SkinOptionsScreen;
//        int i = 0;
//
//        //Custom Capes
//        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(MinecraftCapesConfig.isCapeVisible()).build(this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, Text.of("Custom Capes"), (button, enabled) -> {
//            MinecraftCapesConfig.setCapeVisible(enabled);
//        }));
//        i++;
//
//        //Custom Ears
//        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(MinecraftCapesConfig.isEarsVisible()).build(this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, Text.of("Custom Ears"), (button, enabled) -> {
//            MinecraftCapesConfig.setEarsVisible(enabled);
//        }));
//        i++;
//
//        //Force Reload Profile to get an extra line
//        i++;
//
//        //Reload Profile
//        this.addDrawableChild(new ButtonWidget(this.width / 2 - 75, this.height / 6 + 24 * (i >> 1), 150, 20, Text.of("Reload Profile"), (button) -> {
//            PlayerEventHandler.downloadProfile(PlayerHandler.getFromPlayer(client.player));
//        }));
//        i++;
//
//        //Done
//        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), 200, 20, ScreenTexts.DONE, (button) -> {
//            this.client.openScreen(null);
//        }));
//    }
//
//    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//        this.renderBackground(matrices);
//        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 16777215);
//        super.render(matrices, mouseX, mouseY, delta);
//    }
}
