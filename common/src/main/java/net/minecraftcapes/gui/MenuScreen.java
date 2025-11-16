package net.minecraftcapes.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;

public class MenuScreen extends Screen {
    
    public MenuScreen() {
        super(new TranslatableComponent("category.minecraftcapes.gui"));
    }
    
    @Override
    public void init() {
        //net.minecraft.client.gui.GuiCustomizeSkin;
        int i = 0;

        //Custom Capes
        this.addRenderableWidget(
                CycleButton.onOffBuilder(MinecraftCapesConfig.isCapeVisible())
                        .create(
                                this.width / 2 - 155 + i % 2 * 160,
                                this.height / 6 + 24 * (i >> 1),
                                150,
                                20,
                                Component.nullToEmpty("Custom Capes"),
                                (param1, param2) -> MinecraftCapesConfig.setCapeVisible(!MinecraftCapesConfig.isCapeVisible())
                        )
        );
        i++;

        //Custom Ears
        this.addRenderableWidget(
                CycleButton.onOffBuilder(MinecraftCapesConfig.isEarsVisible())
                        .create(
                                this.width / 2 - 155 + i % 2 * 160,
                                this.height / 6 + 24 * (i >> 1),
                                150,
                                20,
                                Component.nullToEmpty("Custom Ears"),
                                (param1, param2) -> MinecraftCapesConfig.setEarsVisible(!MinecraftCapesConfig.isEarsVisible())
                        )
        );
        i++;

        //Force Reload Profile to get an extra line
        i++;

        //Reload Profile
        this.addRenderableWidget(
                new Button(
                        this.width / 2 - 75,
                        this.height / 6 + 24 * (i >> 1),
                        150,
                        20,
                        Component.nullToEmpty("Reload Profile"),
                        (button -> DownloadManager.prepareDownload(this.minecraft.player.getUUID(), this.minecraft.player.getName().getString(), true))
                )
        );
        i++;

        //Done
        this.addRenderableWidget(
                new Button(
                        this.width / 2 - 100,
                        this.height / 6 + 24 * (i >> 1),
                        200,
                        20,
                        CommonComponents.GUI_DONE,
                        (button) -> this.minecraft.setScreen(null)
                )
        );
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        drawCenteredString(poseStack, this.font, this.title, this.width / 2, 20, 16777215);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }
}
