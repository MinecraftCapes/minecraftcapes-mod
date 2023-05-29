package net.minecraftcapes.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
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
        renderEntityInInventory(
                this.width / 5,
                this.height / 3 + 90,
                60,
                this.minecraft.player
        );
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }
    
    public static void renderEntityInInventory(int leftPos, int topPos, int size, LivingEntity livingEntity) {
        PoseStack $$8 = RenderSystem.getModelViewStack();
        $$8.pushPose();
        $$8.translate((double)leftPos, (double)topPos, 1050.0D);
        $$8.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack $$9 = new PoseStack();
        $$9.translate(0.0D, 0.0D, 1000.0D);
        $$9.scale((float)size, (float)size, (float)size);
        Quaternion $$10 = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion $$11 = Vector3f.XP.rotationDegrees(0f);
        $$10.mul($$11);
        $$9.mulPose($$10);
        float $$12 = livingEntity.yBodyRot;
        float $$13 = livingEntity.getYRot();
        float $$14 = livingEntity.getXRot();
        float $$15 = livingEntity.yHeadRotO;
        float $$16 = livingEntity.yHeadRot;
        livingEntity.yBodyRot = 0f; //180f
        livingEntity.setYRot(0F); //180f
        livingEntity.setXRot(0f);
        livingEntity.yHeadRot = livingEntity.getYRot();
        livingEntity.yHeadRotO = livingEntity.getYRot();
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher $$17 = Minecraft.getInstance().getEntityRenderDispatcher();
        $$11.conj();
        $$17.overrideCameraOrientation($$11);
        $$17.setRenderShadow(false);
        MultiBufferSource.BufferSource $$18 = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> $$17.render(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, $$9, $$18, 15728880));
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
