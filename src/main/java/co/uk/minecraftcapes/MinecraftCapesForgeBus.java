package co.uk.minecraftcapes;

import co.uk.minecraftcapes.capabilities.PlayerHandlerCapability;
import co.uk.minecraftcapes.reference.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MinecraftCapesForgeBus {

    private static final ResourceLocation ID = new ResourceLocation(Reference.MODID, "playerhandler");

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(ID, new PlayerHandlerCapability());
        }
    }
}
