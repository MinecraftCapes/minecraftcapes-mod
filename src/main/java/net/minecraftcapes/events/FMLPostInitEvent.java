package net.minecraftcapes.events;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftcapes.helpers.ScheduleTask;
import net.minecraftcapes.player.render.CapeLayer;
import net.minecraftcapes.player.render.Deadmau5;
import net.minecraftforge.common.MinecraftForge;

public class FMLPostInitEvent {

    public static void init() {
        FMLCommonHandler.instance().bus().register(new ScheduleTask());
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        MinecraftForge.EVENT_BUS.register(new CapeLayer());
        MinecraftForge.EVENT_BUS.register(new Deadmau5());
    }
}