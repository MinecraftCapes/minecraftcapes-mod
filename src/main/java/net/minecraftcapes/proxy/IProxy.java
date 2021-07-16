package net.minecraftcapes.proxy;

public interface IProxy {

    default void init() {}

    default void postInit() {};
}
