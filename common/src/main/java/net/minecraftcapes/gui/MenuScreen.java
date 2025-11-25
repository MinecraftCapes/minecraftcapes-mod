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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;
import org.spongepowered.asm.mixin.Unique;

public class MenuScreen extends Screen {
    
    public MenuScreen() {
        super(new TextComponent("MinecraftCapes"));
    }
    
    @Override
    public void init() {
        int xOffset = this.width / 6 * 4;
        int yOffset = this.height / 3;
        int i = 0;
        
        //Open MinecraftCapes
        this.addRenderableWidget(new Button(xOffset - 75, yOffset, 150, 20, Component.nullToEmpty("Open MinecraftCapes"), (button) -> {
            this.minecraft.setScreen(new ConfirmLinkScreen((openUrl) -> {
                if(openUrl) {
                    Util.getPlatform().openUri("https://minecraftcapes.net");
                }
                
                this.minecraft.setScreen(this);
            }, "https://minecraftcapes.net", true));
        }));
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
        this.addRenderableWidget(new Button(xOffset - 75, yOffset + 24 * (i >> 1), 150, 20, Component.nullToEmpty("Reload Profile"), (button -> {
            DownloadManager.prepareDownload(this.minecraft.player.getUUID(), this.minecraft.player.getName().getString(), true);
        })));
        i++;
        
        //Done
        this.addRenderableWidget(new Button(xOffset - 100, yOffset + 24 * (i >> 1), 200, 20, CommonComponents.GUI_DONE, (button) -> {
            this.minecraft.setScreen(null);
        }));
    }
    
    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        drawCenteredString(poseStack, this.font, this.title.getContents(), this.width / 2, 20, 16777215);
        
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
        PoseStack var2 = RenderSystem.getModelViewStack();
        var2.pushPose();
        var2.translate(x, y, 1050.0F);
        var2.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack var3 = new PoseStack();
        var3.translate(0.0F, 0.0F, 1000.0F);
        var3.scale(scale, scale, scale);
        Quaternion var4 = Vector3f.ZP.rotationDegrees(180.0F);
        var3.mulPose(var4);
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
        EntityRenderDispatcher var11 = Minecraft.getInstance().getEntityRenderDispatcher();
        var11.setRenderShadow(false);
        MultiBufferSource.BufferSource var12 = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> var11.render(param5, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, var3, var12, 15728880));
        var12.endBatch();
        var11.setRenderShadow(true);
        param5.yBodyRot = var6;
        param5.setYRot(var7);
        param5.setXRot(var8);
        param5.yHeadRotO = var9;
        param5.yHeadRot = var10;
        var2.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }
}