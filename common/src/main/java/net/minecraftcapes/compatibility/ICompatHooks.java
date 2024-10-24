package net.minecraftcapes.compatibility;

import net.minecraft.client.renderer.entity.state.PlayerRenderState;

public interface ICompatHooks {

    void onPlayerRender(PlayerRenderState playerRenderState);

}
