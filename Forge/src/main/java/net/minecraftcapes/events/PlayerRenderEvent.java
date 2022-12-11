package net.minecraftcapes.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.world.entity.player.Player;
import net.minecraftcapes.player.PlayerHandler;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerRenderEvent {

    @SubscribeEvent
    public void onPlayerRender(RenderLivingEvent.Pre event) {
        if(event.getEntity() instanceof Player) {
            PoseStack poseStack = event.getPoseStack();
            Player playerEntity = (Player) event.getEntity();
            PlayerHandler playerHandler = PlayerHandler.getFromPlayer(playerEntity);

            if(playerHandler.isUpsideDown()) {
                poseStack.pushPose();
                poseStack.translate(0.0D, playerEntity.getBbHeight() + 0.1F, 0.0D);

                //Rotates the player upside down
                poseStack.mulPose(Axis.XN.rotationDegrees(180F));

                //Removes the current rotation then negates it
                poseStack.mulPose(Axis.YN.rotationDegrees(-playerEntity.yRotO));
                poseStack.mulPose(Axis.YN.rotationDegrees(-playerEntity.yRotO));

                //Flips the rotation again
                poseStack.mulPose(Axis.YN.rotationDegrees(180F));
            }
        }
    }

    @SubscribeEvent
    public void afterPlayerRender(RenderLivingEvent.Post event) {
        if(event.getEntity() instanceof Player) {
            PoseStack poseStack = event.getPoseStack();
            Player player = (Player) event.getEntity();
            PlayerHandler playerHandler = PlayerHandler.getFromPlayer(player);

            if(playerHandler.isUpsideDown()) {
                poseStack.translate(0.0D, -player.getBbHeight() - 0.1F, 0.0D);
                poseStack.popPose();
            }
        }
    }
}
