package net.minecraftcapes.events;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftcapes.player.PlayerHandler;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerRenderEvent {

    @SubscribeEvent
    public void onPlayerRender(RenderLivingEvent.Pre event) {
        if(event.getEntity() instanceof PlayerEntity) {
            MatrixStack matrixStack = event.getMatrixStack();
            PlayerEntity playerEntity = (PlayerEntity) event.getEntity();
            PlayerHandler playerHandler = PlayerHandler.getFromPlayer(playerEntity);

            if(playerHandler.isUpsideDown()) {
                matrixStack.push();
                matrixStack.translate(0.0D, playerEntity.getHeight() + 0.1F, 0.0D);

                //Rotates the player upside down
                matrixStack.rotate(Vector3f.XN.rotationDegrees(180F));

                //Removes the current rotation then negates it
                matrixStack.rotate(Vector3f.YN.rotationDegrees(-playerEntity.rotationYaw));
                matrixStack.rotate(Vector3f.YN.rotationDegrees(-playerEntity.rotationYaw));

                //Flips the rotation again
                matrixStack.rotate(Vector3f.YN.rotationDegrees(180F));
            }
        }
    }

    @SubscribeEvent
    public void afterPlayerRender(RenderLivingEvent.Post event) {
        if(event.getEntity() instanceof PlayerEntity) {
            MatrixStack matrixStack = event.getMatrixStack();
            PlayerEntity playerEntity = (PlayerEntity) event.getEntity();
            PlayerHandler playerHandler = PlayerHandler.getFromPlayer(playerEntity);

            if(playerHandler.isUpsideDown()) {
                matrixStack.translate(0.0D, -playerEntity.getHeight() - 0.1F, 0.0D);
                matrixStack.pop();
            }
        }
    }
}
