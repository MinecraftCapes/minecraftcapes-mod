package net.minecraftcapes.gui;

import com.mojang.math.Axis;
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
        renderEntity(
                guiGraphics,
                this.width / 3,
                this.height,
                this.minecraft.player
        );
    }
    
    @Unique
    private void renderEntity(GuiGraphics guiGraphics, int x2, int y2, LivingEntity entity) {
        Quaternionf quaternionf = (new Quaternionf()).rotateZ((float)Math.PI);
        
        EntityRenderState entityrenderstate = extractRenderState(entity);
        if (entityrenderstate instanceof LivingEntityRenderState livingentityrenderstate) {
            livingentityrenderstate.bodyRot = 0F;
            livingentityrenderstate.yRot = 0f;
            livingentityrenderstate.xRot = 0f;
            livingentityrenderstate.boundingBoxWidth /= livingentityrenderstate.scale;
            livingentityrenderstate.boundingBoxHeight /= livingentityrenderstate.scale;
            livingentityrenderstate.scale = 1.0F;
        }
        
        Vector3f vector3f = new Vector3f(0.0F, entityrenderstate.boundingBoxHeight / 2.0625F, 0.0F);
        guiGraphics.submitEntityRenderState(entityrenderstate, 60f, vector3f, quaternionf, null, 0, 0, x2, y2);
    }
    
    @Unique
    private EntityRenderState extractRenderState(LivingEntity livingEntity) {
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super LivingEntity, ?> entityrenderer = entityrenderdispatcher.getRenderer(livingEntity);
        EntityRenderState entityrenderstate = entityrenderer.createRenderState(livingEntity, 1.0F);
        entityrenderstate.lightCoords = 15728880;
        entityrenderstate.shadowPieces.clear();
        entityrenderstate.outlineColor = 0;
        return entityrenderstate;
    }
}