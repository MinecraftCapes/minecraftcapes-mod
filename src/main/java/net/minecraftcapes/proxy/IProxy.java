package net.minecraftcapes.proxy;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

public interface IProxy {

    default void init() {};

    default void clientSetup(FMLCommonSetupEvent event) {};

    default void enqueueIMC(InterModEnqueueEvent event) {};
}
