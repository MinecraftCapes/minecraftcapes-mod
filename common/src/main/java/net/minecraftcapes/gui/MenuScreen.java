package net.minecraftcapes.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Unique;

public class MenuScreen extends Screen {
    
    private static final Component TITLE = Component.nullToEmpty("MinecraftCapes");
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    
    public MenuScreen() {
        super(TITLE);
    }
    
    @Override
    protected void init() {
        this.layout.addTitleHeader(TITLE, this.font);
        
        GridLayout gridLayout = new GridLayout();
        gridLayout.defaultCellSetting().padding(100, 4, 4, 0);
        GridLayout.RowHelper helper = gridLayout.createRowHelper(1);
        
        this.layout.addToContents(gridLayout);
        
        helper.addChild(Button.builder(Component.nullToEmpty("Open MinecraftCapes"), ConfirmLinkScreen.confirmLink(this, "https://minecraftcapes.net"))
                .build(), 2);
        helper.addChild(Button.builder(Component.nullToEmpty("Reload Profile"), _ -> DownloadManager.prepareDownload(this.minecraft.player.getUUID(), this.minecraft.player.getName()
                .getString(), true)).build(), 2);
        helper.addChild(CycleButton.onOffBuilder(MinecraftCapesConfig.isCapeVisible())
                .create(Component.nullToEmpty("Custom Capes"), (_, value) -> MinecraftCapesConfig.setCapeVisible(value)), 2);
        helper.addChild(CycleButton.onOffBuilder(MinecraftCapesConfig.isEarsVisible())
                .create(Component.nullToEmpty("Custom Ears"), (_, value) -> MinecraftCapesConfig.setEarsVisible(value)), 2);
        
        this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, _ -> this.onClose()).width(200).build());
        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }
    
    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
    }
    
    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        this.renderEntity(
                graphics,
                this.width / 2,
                this.height,
                this.minecraft.player
        );
    }

    @Unique
    private void renderEntity(GuiGraphicsExtractor guiGraphics, int x, int y, LivingEntity entity) {
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
        guiGraphics.entity(entityrenderstate, 60f, vector3f, quaternionf, null, 0, 0, x, y);
    }

    @Unique
    private EntityRenderState extractRenderState(LivingEntity livingEntity) {
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super LivingEntity, ?> entityrenderer = entityrenderdispatcher.getRenderer(livingEntity);
        EntityRenderState entityrenderstate = entityrenderer.createRenderState(livingEntity, 1.0F);
        entityrenderstate.shadowPieces.clear();
        entityrenderstate.outlineColor = 0;
        return entityrenderstate;
    }
}