package net.minecraftcapes.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;
import org.joml.Quaternionf;

public class MenuScreen extends Screen {

    public MenuScreen() {
        super(Component.translatable("category.minecraftcapes.gui"));
    }

    protected void init() {
        int i = 0;
        
        //Reload Profile
        this.addRenderableWidget(Button.builder(Component.nullToEmpty("Open MinecraftCapes"), (button) -> {
            this.minecraft.setScreen(new ConfirmLinkScreen((shouldOpen) -> {
                if(shouldOpen) {
                    Util.getPlatform().openUri("https://minecraftcapes.net");
                }
                this.minecraft.setScreen(this);
            }, "https://minecraftcapes.net", true));
        }).bounds(this.width / 3 * 2 - 75, this.height / 3, 150, 20).build());
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
            DownloadManager.prepareDownload(this.minecraft.player, true);
        }).bounds(this.width / 3 * 2 - 75, this.height / 3 + 24 * (i >> 1), 150, 20).build());
        i++;

        //Done
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            this.minecraft.setScreen(null);
        }).bounds(this.width / 3 * 2 - 100, this.height / 3 + 24 * (i >> 1), 200, 20).build());
    }

    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        int guiScale = (int) this.minecraft.getWindow().getGuiScale();
        System.out.println(guiScale);
        this.renderBackground(poseStack);
        drawCenteredString(poseStack, this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        renderPlayer(
                this.width / 5,
                this.height / 3 + 90,
                60,
                this.minecraft.player
        );
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }
    
    public static void renderPlayer(int $$0, int $$1, int $$2, LivingEntity livingEntity) {
        float $$6 = 0f;
        float $$7 = 0f;
        PoseStack $$8 = RenderSystem.getModelViewStack();
        $$8.pushPose();
        $$8.translate((float)$$0, (float)$$1, 1050.0F);
        $$8.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack $$9 = new PoseStack();
        $$9.translate(0.0F, 0.0F, 1000.0F);
        $$9.scale((float)$$2, (float)$$2, (float)$$2);
        Quaternionf $$10 = (new Quaternionf()).rotateZ(3.1415927F);
        Quaternionf $$11 = (new Quaternionf()).rotateX($$7 * 20.0F * 0.017453292F);
        $$10.mul($$11);
        $$9.mulPose($$10);
        float $$12 = livingEntity.yBodyRot;
        float $$13 = livingEntity.getYRot();
        float $$14 = livingEntity.getXRot();
        float $$15 = livingEntity.yHeadRotO;
        float $$16 = livingEntity.yHeadRot;
        livingEntity.yBodyRot = 0f;
        livingEntity.setYRot(0f);
        livingEntity.setXRot(0f);
        livingEntity.yHeadRot = livingEntity.getYRot();
        livingEntity.yHeadRotO = livingEntity.getYRot();
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher $$17 = Minecraft.getInstance().getEntityRenderDispatcher();
        $$11.conjugate();
        $$17.overrideCameraOrientation($$11);
        $$17.setRenderShadow(false);
        MultiBufferSource.BufferSource $$18 = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            $$17.render(livingEntity, 0.0, 0.0, 0.0, 0.0F, 1.0F, $$9, $$18, 15728880);
        });
        $$18.endBatch();
        $$17.setRenderShadow(true);
        livingEntity.yBodyRot = $$12;
        livingEntity.setYRot($$13);
        livingEntity.setXRot($$14);
        livingEntity.yHeadRotO = $$15;
        livingEntity.yHeadRot = $$16;
        $$8.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }
    
    private Component getButtonString(String buttonText) {
        return Component.nullToEmpty(buttonText);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
}
