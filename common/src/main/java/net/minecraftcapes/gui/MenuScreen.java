package net.minecraftcapes.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MenuScreen extends Screen {

    public MenuScreen() {
        super(Component.translatable("category.minecraftcapes.gui"));
    }

    protected void init() {
        int i = 0;
        
        //Reload Profile
        this.addRenderableWidget(Button.builder(Component.nullToEmpty("Open MinecraftCapes"),
                ConfirmLinkScreen.confirmLink(this, "https://minecraftcapes.net")
        ).bounds(this.width / 3 * 2 - 75, this.height / 3, 150, 20).build());
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
        this.addRenderableWidget(Button.builder(Component.nullToEmpty("Reload Profile"), (button) -> {
            DownloadManager.prepareDownload(
                    this.minecraft.player.getGameProfile().id(),
                    this.minecraft.player.getGameProfile().name(),
                    true);
        }).bounds(this.width / 3 * 2 - 75, this.height / 3 + 24 * (i >> 1), 150, 20).build());
        i++;

        //Done
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            this.minecraft.setScreen(null);
        }).bounds(this.width / 3 * 2 - 100, this.height / 3 + 24 * (i >> 1), 200, 20).build());
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        renderPlayer(
                guiGraphics,
                0,
                0,
                this.width / 3,
                this.height + 90,
                60,
                this.minecraft.player
        );
    }
    
    public static void renderPlayer(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int size, LivingEntity livingEntiy) {
        Quaternionf $$9 = (new Quaternionf()).rotateZ(3.1415927F);
        Quaternionf $$10 = (new Quaternionf()).rotateX(0);
        $$9.mul($$10);
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
        Vector3f vector3f = new Vector3f(0.0F, livingEntiy.getBbHeight() / 4, 0.0F);
        InventoryScreen.renderEntityInInventory(guiGraphics, x1, y1, x2, y2, size, vector3f, $$9, $$10, livingEntiy);
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
