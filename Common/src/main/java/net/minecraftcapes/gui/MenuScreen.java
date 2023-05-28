package net.minecraftcapes.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;

public class MenuScreen extends Screen {

    public MenuScreen() {
        super(new TranslatableComponent("category.minecraftcapes.gui"));
    }

    protected void init() {
        int i = 0;
        
        //Reload Profile
        this.addRenderableWidget(new Button(this.width / 3 * 2 - 75, this.height / 3, 150, 20, Component.nullToEmpty("Open MinecraftCapes"), (button) -> {
            this.minecraft.setScreen(new ConfirmLinkScreen((shouldOpen) -> {
                if (shouldOpen) {
                    Util.getPlatform().openUri("https://minecraftcapes.net");
                }
                this.minecraft.setScreen(this);
            }, "https://minecraftcapes.net", true));
        }));
        i++;
        
        //For custom cape/ears onto new line
        i++;
        
        //Custom Capes
        this.addRenderableWidget(CycleButton.onOffBuilder(MinecraftCapesConfig.isCapeVisible()).create(this.width / 3 * 2 - 155 + i % 2 * 160, this.height / 3 + 24 * (i >> 1), 150, 20, getButtonString("Custom Capes"), (button, enabled) -> {
            MinecraftCapesConfig.setCapeVisible(enabled);
        }));
        i++;

        //Custom Ears
        this.addRenderableWidget(CycleButton.onOffBuilder(MinecraftCapesConfig.isEarsVisible()).create(this.width / 3 * 2 - 155 + i % 2 * 160, this.height / 3 + 24 * (i >> 1), 150, 20, getButtonString("Custom Ears"), (button, enabled) -> {
            MinecraftCapesConfig.setEarsVisible(enabled);
        }));
        i++;

        //Force Reload Profile to get an extra line
        i++;

        //Reload Profile
        this.addRenderableWidget(new Button(this.width / 3 * 2 - 75, this.height / 3 + 24 * (i >> 1), 150, 20, Component.nullToEmpty("Reload Profile"), (button) -> {
            DownloadManager.prepareDownload(this.minecraft.player, true);
        }));
        i++;

        //Done
        this.addRenderableWidget(new Button(this.width / 3 * 2 - 100, this.height / 3 + 24 * (i >> 1), 200, 20, CommonComponents.GUI_DONE, (button) -> {
            this.minecraft.setScreen(null);
        }));
    }

    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        drawCenteredString(poseStack, this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        renderPlayer(
                this.width / 5,
                this.height - 60,
                60,
                this.minecraft.player
        );
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }
    
    public static void renderPlayer(int leftPos, int topPos, int size, LivingEntity livingEntiy) {
        float $$11 = livingEntiy.yBodyRot;
        float $$12 = livingEntiy.getYRot();
        float $$13 = livingEntiy.getXRot();
        float $$14 = livingEntiy.yHeadRotO;
        float $$15 = livingEntiy.yHeadRot;
        livingEntiy.yBodyRot = 0;
        livingEntiy.setYRot(0);
        livingEntiy.setXRot(0);
        livingEntiy.yHeadRot = livingEntiy.getYRot();
        livingEntiy.yHeadRotO = livingEntiy.getYRot();
        InventoryScreen.renderEntityInInventory(leftPos, topPos, size, 0f, 0f, livingEntiy);
        livingEntiy.yBodyRot = $$11;
        livingEntiy.setYRot($$12);
        livingEntiy.setXRot($$13);
        livingEntiy.yHeadRotO = $$14;
        livingEntiy.yHeadRot = $$15;
    }
    
    private Component getButtonString(String buttonText) {
        return Component.nullToEmpty(buttonText);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
}
