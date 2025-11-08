package net.minecraftcapes.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
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

        renderEntity(
                guiGraphics,
                0,
                0,
                this.width / 3,
                this.height,
                60,
                0.0625F,
                this.minecraft.player
        );
    }

    private void renderEntity(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int scale, float yOffset, LivingEntity entity) {
        Quaternionf quaternionf = (new Quaternionf()).rotateZ((float)Math.PI);
        Quaternionf quaternionf1 = (new Quaternionf()).rotateX(0f);
        quaternionf.mul(quaternionf1);

        EntityRenderState entityrenderstate = extractRenderState(entity);
        if (entityrenderstate instanceof LivingEntityRenderState livingentityrenderstate) {
            livingentityrenderstate.bodyRot = 0F;
            livingentityrenderstate.yRot = 0f;
            livingentityrenderstate.xRot = 0f;
            livingentityrenderstate.boundingBoxWidth /= livingentityrenderstate.scale;
            livingentityrenderstate.boundingBoxHeight /= livingentityrenderstate.scale;
            livingentityrenderstate.scale = 1.0F;
        }

        Vector3f vector3f = new Vector3f(0.0F, entityrenderstate.boundingBoxHeight / 2.0F + yOffset, 0.0F);
        guiGraphics.submitEntityRenderState(entityrenderstate, (float)scale, vector3f, quaternionf, quaternionf1, x1, y1, x2, y2);
    }

    private EntityRenderState extractRenderState(LivingEntity livingEntity) {
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super LivingEntity, ?> entityrenderer = entityrenderdispatcher.getRenderer(livingEntity);
        EntityRenderState entityrenderstate = entityrenderer.createRenderState(livingEntity, 1.0F);
        entityrenderstate.lightCoords = 15728880;
        entityrenderstate.shadowPieces.clear();
        entityrenderstate.outlineColor = 0;
        return entityrenderstate;
    }
    
    private Component getButtonString(String buttonText) {
        return Component.nullToEmpty(buttonText);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
}
