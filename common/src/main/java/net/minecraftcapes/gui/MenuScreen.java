package net.minecraftcapes.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.MenuTabBar;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftcapes.gui.tabs.GeneralTab;
import net.minecraftcapes.gui.tabs.LinksTab;
import net.minecraftcapes.gui.tabs.OptionsTab;
import net.minecraftcapes.gui.tabs.WardrobeTab;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;

public class MenuScreen extends Screen {
    
    private static final Component TITLE = Component.translatable("gui.minecraftcapes.title");
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
    private @Nullable MenuTabBar tabNavigationBar;
    
    public MenuScreen() {
        super(TITLE);
    }
    
    @Override
    protected void init() {
        this.tabNavigationBar = MenuTabBar.builder(this.tabManager, this.width)
                .addTabs(
                        new GeneralTab(this, minecraft.player),
                        new WardrobeTab(this, minecraft.player),
                        new OptionsTab(),
                        new LinksTab(this)
                )
                .build();
        this.setFocused(this.tabNavigationBar);
        this.addRenderableWidget(this.tabNavigationBar);
        this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, _ -> this.onClose()).width(200).build());
        this.layout.visitWidgets(button -> {
            button.setTabOrderGroup(1);
            this.addRenderableWidget(button);
        });
        this.tabNavigationBar.selectTab(0, false);
        
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        if (this.tabNavigationBar != null) {
            this.tabNavigationBar.arrangeElements(this.width);
            int tabAreaTop = this.tabNavigationBar.getRectangle().bottom();
            ScreenRectangle tabArea = new ScreenRectangle(0, tabAreaTop, this.width, this.height - this.layout.getFooterHeight() - tabAreaTop);
            this.tabManager.setTabArea(tabArea);
            this.layout.setHeaderHeight(tabAreaTop);
            this.layout.arrangeElements();
        }
    }
    
    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        
        if(!(this.tabManager.getCurrentTab() instanceof WardrobeTab)) {
            this.extractEntityRenderState(
                    graphics,
                    this.width / 2,
                    this.height,
                    this.minecraft.player
            );
        }
    }

    @Unique
    private void extractEntityRenderState(GuiGraphicsExtractor guiGraphics, int x, int y, LivingEntity entity) {
        Quaternionf quaternionf = (new Quaternionf()).rotateZ((float)Math.PI);
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super LivingEntity, ?> entityrenderer = entityrenderdispatcher.getRenderer(entity);
        EntityRenderState entityrenderstate = entityrenderer.createRenderState(entity, 1.0F);
        entityrenderstate.shadowPieces.clear();
        entityrenderstate.outlineColor = 0;
        
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
}