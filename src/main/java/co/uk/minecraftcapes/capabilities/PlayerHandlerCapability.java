package co.uk.minecraftcapes.capabilities;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerHandlerCapability implements ICapabilityProvider {

    @CapabilityInject(PlayerHandler.class)
    public static Capability<PlayerHandler> capability;

    private LazyOptional<PlayerHandler> instance = LazyOptional.of(capability::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return capability.orEmpty(capability, instance).cast();
    }
}
