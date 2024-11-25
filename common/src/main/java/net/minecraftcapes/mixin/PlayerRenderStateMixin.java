package net.minecraftcapes.mixin;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraftcapes.ExtendedPlayerRenderState;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Setter
@Getter
@Mixin(PlayerRenderState.class)
public class PlayerRenderStateMixin implements ExtendedPlayerRenderState {

    @Unique
    private PlayerHandler minecraftCapes$playerHandler;

}