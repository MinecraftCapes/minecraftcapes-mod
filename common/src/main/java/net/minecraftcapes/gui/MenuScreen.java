package net.minecraftcapes.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.components.tabs.MenuTabBar;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
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
import net.minecraftcapes.player.PlayerHandler;
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
                .addTabs(new MenuScreen.GeneralTab(), new OptionsTab(), new MenuScreen.LinksTab())
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
    
    private class GeneralTab extends GridLayoutTab {
        public GeneralTab() {
            super(Component.translatable("gui.minecraftcapes.tab.general"));
            
            this.layout.defaultCellSetting().padding(100, 4, 4, 0);
            GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
            
            // Open MinecraftCapes
            Button openMinecraftCapes = Button.builder(
                    Component.translatable("button.minecraftcapes.minecraftcapes"),
                    ConfirmLinkScreen.confirmLink(MenuScreen.this, "https://minecraftcapes.net")
            )
            .build();
            
            // Reload Profile
            Button reloadProfile = Button.builder(
                    Component.translatable("button.minecraftcapes.reload"),
                    _ -> PlayerHandler.remove(MenuScreen.this.minecraft.player.getUUID())
            )
            .build();
            
            Button reloadAllProfiles = Button.builder(
                    Component.translatable("button.minecraftcapes.reload_all"),
                    _ -> PlayerHandler.clearAll()
            )
            .build();
            
            // Tool Tips
            openMinecraftCapes.setTooltip(Tooltip.create(Component.translatable("button.minecraftcapes.minecraftcapes.tooltip")));
            reloadProfile.setTooltip(Tooltip.create(Component.translatable("button.minecraftcapes.reload.tooltip")));
            reloadAllProfiles.setTooltip(Tooltip.create(Component.translatable("button.minecraftcapes.reload_all.tooltip")));
            
            // Add Buttons
            helper.addChild(openMinecraftCapes);
            helper.addChild(reloadProfile);
            helper.addChild(reloadAllProfiles);
        }
    }
    
    private static class OptionsTab extends GridLayoutTab {
        public OptionsTab() {
            super(Component.translatable("gui.minecraftcapes.tab.options"));
            
            this.layout.defaultCellSetting().padding(100, 4, 4, 0);
            GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
            
            CycleButton<Boolean> customCapes = CycleButton.onOffBuilder(MinecraftCapesConfig.isCapeVisible())
            .create(
                Component.translatable("button.minecraftcapes.custom_capes"),
                (_, value) -> MinecraftCapesConfig.setCapeVisible(value)
            );
            
            CycleButton<Boolean> customEars = CycleButton.onOffBuilder(MinecraftCapesConfig.isEarsVisible())
            .create(
                Component.translatable("button.minecraftcapes.custom_ears"),
                (_, value) -> MinecraftCapesConfig.setEarsVisible(value)
            );
            
            // Tool Tips
            customCapes.setTooltip(Tooltip.create(Component.translatable("button.minecraftcapes.custom_capes.tooltip")));
            customEars.setTooltip(Tooltip.create(Component.translatable("button.minecraftcapes.custom_ears.tooltip")));
            
            // Add Buttons
            helper.addChild(customCapes);
            helper.addChild(customEars);
        }
    }
    
    private class LinksTab extends GridLayoutTab {
        public LinksTab() {
            super(Component.translatable("gui.minecraftcapes.tab.links"));
            
            this.layout.defaultCellSetting().padding(100, 4, 4, 0);
            GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
            
            // MinecraftCapes
            Button openMinecraftCapes = Button.builder(
                    Component.translatable("button.minecraftcapes.minecraftcapes"),
                    ConfirmLinkScreen.confirmLink(MenuScreen.this, "https://minecraftcapes.net")
            )
            .build();
            
            Button openDiscord = Button.builder(
                    Component.translatable("button.minecraftcapes.discord"),
                    ConfirmLinkScreen.confirmLink(MenuScreen.this, "https://discord.gg/minecraftcapes")
            )
            .build();
            
            Button openGitHub = Button.builder(
                    Component.translatable("button.minecraftcapes.github"),
                    ConfirmLinkScreen.confirmLink(MenuScreen.this, "https://github.com/minecraftcapes")
            )
            .build();
            
            // Add Buttons
            helper.addChild(openMinecraftCapes);
            helper.addChild(openDiscord);
            helper.addChild(openGitHub);
        }
    }
    
}