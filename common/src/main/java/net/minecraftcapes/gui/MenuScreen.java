package net.minecraftcapes.gui;

import com.mojang.math.Axis;
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
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Unique;

public class MenuScreen extends Screen {
    
    public MenuScreen() {
        super(Component.nullToEmpty("MinecraftCapes"));
    }

    @Override
    public void init() {
        int xOffset = this.width / 6 * 4;
        int yOffset = this.height / 3;
        int i = 0;
        
        //Open MinecraftCapes
        this.addRenderableWidget(
                Button.builder(
                        Component.nullToEmpty("Open MinecraftCapes"),
                        ConfirmLinkScreen.confirmLink(this, "https://minecraftcapes.net")
                )
                .bounds(xOffset - 75, yOffset, 150, 20)
                .build()
        );
        i++;
        
        //For custom cape/ears onto new line
        i++;

        //Custom Capes
        this.addRenderableWidget(
                CycleButton
                    .onOffBuilder(MinecraftCapesConfig.isCapeVisible())
                    .create(
                            xOffset - 155 + i % 2 * 160,
                            yOffset + 24 * (i >> 1),
                            150,
                            20,
                            Component.nullToEmpty("Custom Capes"),
                            (button, value) -> MinecraftCapesConfig.setCapeVisible(value)
                    )
        );
        i++;

        //Custom Ears
        this.addRenderableWidget(
                CycleButton
                        .onOffBuilder(MinecraftCapesConfig.isEarsVisible())
                        .create(
                                xOffset - 155 + i % 2 * 160,
                                yOffset + 24 * (i >> 1),
                                150,
                                20,
                                Component.nullToEmpty("Custom Ears"),
                                (button, value) -> MinecraftCapesConfig.setEarsVisible(value)
                        )
        );
        i++;

        //Force Reload Profile to get an extra line
        i++;

        //Reload Profile
        this.addRenderableWidget(Button.builder(Component.nullToEmpty("Reload Profile"), (button -> {
            DownloadManager.prepareDownload(this.minecraft.player.getUUID(), this.minecraft.player.getName().getString(), true);
        })).bounds(xOffset - 75, yOffset + 24 * (i >> 1), 150, 20).build());
        i++;

        //Done
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            this.minecraft.setScreen(null);
        }).bounds(xOffset - 100, yOffset + 24 * (i >> 1), 200, 20).build());
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, -1);
        renderEntity(guiGraphics);
    }
    
    @Unique
    private void renderEntity(GuiGraphics guiGraphics) {
        LivingEntity entity = this.minecraft.player;
        
        float yBodyRot = entity.yBodyRot;
        float yRot = entity.getYRot();
        float xRot = entity.getXRot();
        float yHeadRot0 = entity.yHeadRotO;
        float yHeadRot = entity.yHeadRot;
        entity.yBodyRot = 0.0F;
        entity.setYRot(0.0F);
        entity.setXRot(0.0F);
        entity.yHeadRot = entity.getYRot();
        entity.yHeadRotO = entity.getYRot();
        
        InventoryScreen.renderEntityInInventory(
                guiGraphics,
                0,
                0,
                this.width / 3,
                this.height + 90,
                60,
                new Vector3f(),
                Axis.ZP.rotationDegrees(180f),
                null,
                this.minecraft.player
        );
        
        entity.yBodyRot = yBodyRot;
        entity.setYRot(yRot);
        entity.setXRot(xRot);
        entity.yHeadRotO = yHeadRot0;
        entity.yHeadRot = yHeadRot;
    }
}