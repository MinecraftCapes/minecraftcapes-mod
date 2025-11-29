package net.minecraftcapes.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.ConfirmOpenLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.DownloadManager;

public class MenuScreen extends Screen {

    private enum GuiOption {
        CAPE,
        EARS
    }

    public MenuScreen() {
        super(new StringTextComponent("MinecraftCapes"));
    }

    @Override
    public void init() {
        int xOffset = this.width / 6 * 4;
        int yOffset = this.height / 3;
        int i = 0;

        //Open MinecraftCapes
        this.addButton(new Button(xOffset - 75, yOffset, 150, 20, ITextComponent.nullToEmpty("Open MinecraftCapes"), (button) -> {
            this.minecraft.setScreen(new ConfirmOpenLinkScreen((openUrl) -> {
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
        this.addButton(new Button(xOffset - 155 + i % 2 * 160, yOffset + 24 * (i >> 1), 150, 20, getMessage(GuiOption.CAPE), (button) -> {
            MinecraftCapesConfig.setCapeVisible(!MinecraftCapesConfig.isCapeVisible());
            button.setMessage(getMessage(GuiOption.CAPE));
        }));
        i++;

        //Custom Ears
        this.addButton(new Button(xOffset - 155 + i % 2 * 160, yOffset + 24 * (i >> 1), 150, 20, getMessage(GuiOption.EARS), (button -> {
            MinecraftCapesConfig.setEarsVisible(!MinecraftCapesConfig.isEarsVisible());
            button.setMessage(getMessage(GuiOption.EARS));
        })));
        i++;

        //Force Reload Profile to get an extra line
        i++;

        //Reload Profile
        this.addButton(new Button(xOffset - 75, yOffset + 24 * (i >> 1), 150, 20, ITextComponent.nullToEmpty("Reload Profile"), (button -> {
            DownloadManager.prepareDownload(this.minecraft.player.getUUID(), this.minecraft.player.getName().getString(), true);
        })));
        i++;

        //Done
        this.addButton(new Button(xOffset - 100, yOffset + 24 * (i >> 1), 200, 20, DialogTexts.GUI_DONE, (button) -> {
            this.minecraft.setScreen(null);
        }));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        drawCenteredString(matrixStack, this.font, this.title.getContents(), this.width / 2, 20, 16777215);

        renderPlayer(
                this.width / 6,
                this.height / 3 + 90,
                60,
                this.minecraft.player
        );

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void renderPlayer(int x, int y, int scale, LivingEntity livingEntity) {
        RenderSystem.pushMatrix();
        RenderSystem.translatef(x, y, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(0.0F, 0.0F, 1000.0F);
        matrixStack.scale(scale, scale, scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        matrixStack.mulPose(quaternion);
        float yBodyRot = livingEntity.yBodyRot;
        float yRot = livingEntity.yRot;
        float xRot = livingEntity.xRot;
        float yHeadRotO = livingEntity.yHeadRotO;
        float yHeadRot = livingEntity.yHeadRot;
        livingEntity.yBodyRot = 0.0F;
        livingEntity.yRot = 0.0F;
        livingEntity.xRot = 0.0F;
        livingEntity.yHeadRot = livingEntity.yRot;
        livingEntity.yHeadRotO = livingEntity.yRot;
        EntityRendererManager entityRendererManager = Minecraft.getInstance().getEntityRenderDispatcher();
        entityRendererManager.setRenderShadow(false);
        IRenderTypeBuffer.Impl iRenderTypeBuffer = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> entityRendererManager.render(livingEntity, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, matrixStack, iRenderTypeBuffer, 15728880));
        iRenderTypeBuffer.endBatch();
        entityRendererManager.setRenderShadow(true);
        livingEntity.yBodyRot = yBodyRot;
        livingEntity.yRot = yRot;
        livingEntity.xRot = xRot;
        livingEntity.yHeadRotO = yHeadRotO;
        livingEntity.yHeadRot = yHeadRot;
        RenderSystem.popMatrix();
    }

    private ITextComponent getMessage(GuiOption option) {
        ITextComponent onOff;

        String buttonText = option.name();
        boolean value = false;
        if(option.equals(GuiOption.CAPE)) {
            buttonText = "Custom Capes";
            value = MinecraftCapesConfig.isCapeVisible();
        } else if(option.equals(GuiOption.EARS)) {
            buttonText = "Custom Ears";
            value = MinecraftCapesConfig.isEarsVisible();
        }

        if(value) {
            onOff = DialogTexts.OPTION_ON;
        } else {
            onOff = DialogTexts.OPTION_OFF;
        }

        return ITextComponent.nullToEmpty(buttonText + ": " + onOff.getString());
    }
}
