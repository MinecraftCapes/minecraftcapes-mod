package net.minecraftcapes.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;
import org.joml.Quaternionf;
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
        this.addRenderableWidget(Button.builder(Component.nullToEmpty("Open MinecraftCapes"), (button) -> {
            this.minecraft.setScreen(new ConfirmLinkScreen((openUrl) -> {
                if(openUrl) {
                    Util.getPlatform().openUri("https://minecraftcapes.net");
                }
                
                this.minecraft.setScreen(this);
            }, "https://minecraftcapes.net", true));
        }).bounds(xOffset - 75, yOffset, 150, 20).build());
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
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        drawCenteredString(poseStack, this.font, this.title, this.width / 2, 20, 16777215);
        
        renderPlayer(
                this.width / 6,
                this.height / 3 + 90,
                60,
                this.minecraft.player
        );
        
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }
    
    @Unique
    private void renderPlayer(int x, int y, int scale, LivingEntity param5) {
        PoseStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushPose();
        poseStack.translate(x, y, 1050.0F);
        poseStack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack poseStack1 = new PoseStack();
        poseStack1.translate(0.0F, 0.0F, 1000.0F);
        poseStack1.scale(scale, scale, scale);
        Quaternionf quaternionf = Axis.ZP.rotationDegrees(180f);
        poseStack1.mulPose(quaternionf);
        float var6 = param5.yBodyRot;
        float var7 = param5.getYRot();
        float var8 = param5.getXRot();
        float var9 = param5.yHeadRotO;
        float var10 = param5.yHeadRot;
        param5.yBodyRot = 0.0F;
        param5.setYRot(0.0F);
        param5.setXRot(0.0F);
        param5.yHeadRot = param5.getYRot();
        param5.yHeadRotO = param5.getYRot();
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        entityrenderdispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> entityrenderdispatcher.render(param5, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, poseStack1, bufferSource, 15728880));
        bufferSource.endBatch();
        entityrenderdispatcher.setRenderShadow(true);
        param5.yBodyRot = var6;
        param5.setYRot(var7);
        param5.setXRot(var8);
        param5.yHeadRotO = var9;
        param5.yHeadRot = var10;
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }
}