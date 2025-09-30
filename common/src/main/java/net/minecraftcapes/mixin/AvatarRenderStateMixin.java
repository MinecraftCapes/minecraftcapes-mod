package net.minecraftcapes.mixin;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraftcapes.ExtendedAvatarRenderState;
import net.minecraftcapes.player.PlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Setter
@Getter
@Mixin(AvatarRenderState.class)
public class AvatarRenderStateMixin implements ExtendedAvatarRenderState {

    @Unique
    private PlayerHandler minecraftCapes$playerHandler;

}