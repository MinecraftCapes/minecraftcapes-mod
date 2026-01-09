package net.minecraftcapes.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
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
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

public class MenuScreen extends OptionsSubScreen {
    
    protected @Nullable OptionsList list;
    public final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    
    public MenuScreen() {
        super(null, Minecraft.getInstance().options, Component.nullToEmpty("MinecraftCapes"));
    }
    
    @Override
    protected void addContents() {
        this.list = this.layout.addToContents(new OptionsList(this.minecraft, this.width, this));
        this.addOptions();
    }
    
    @Override
    protected void addOptions() {
        List<AbstractWidget> widgets = new ArrayList<>();
        
        widgets.add(Button.builder(Component.nullToEmpty("Open MinecraftCapes"), ConfirmLinkScreen.confirmLink(this, "https://minecraftcapes.net"))
                .build());
        widgets.add(Button.builder(Component.nullToEmpty("Reload Profile"), _ -> DownloadManager.prepareDownload(this.minecraft.player.getUUID(), this.minecraft.player.getName()
                .getString(), true)).build());
        widgets.add(CycleButton.onOffBuilder(MinecraftCapesConfig.isCapeVisible())
                .create(Component.nullToEmpty("Custom Capes"), (_, value) -> MinecraftCapesConfig.setCapeVisible(value)));
        widgets.add(CycleButton.onOffBuilder(MinecraftCapesConfig.isEarsVisible())
                .create(Component.nullToEmpty("Custom Ears"), (_, value) -> MinecraftCapesConfig.setEarsVisible(value)));
        
        this.list.addSmall(widgets);
    }
    
    @Override
    public void render(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float a) {
        super.render(graphics, mouseX, mouseY, a);
        this.renderEntity(
                graphics,
                this.width / 2,
                this.height,
                this.minecraft.player
        );
    }

    @Unique
    private void renderEntity(GuiGraphics guiGraphics, int x, int y, LivingEntity entity) {
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
        guiGraphics.submitEntityRenderState(entityrenderstate, 60f, vector3f, quaternionf, null, 0, 0, x, y);
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