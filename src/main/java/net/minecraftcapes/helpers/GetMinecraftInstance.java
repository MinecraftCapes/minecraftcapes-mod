package net.minecraftcapes.helpers;

import net.minecraft.client.GameStartupError;
import net.minecraft.client.Minecraft;

import java.lang.reflect.Field;

public class GetMinecraftInstance {

    private static Minecraft instance = null;

    public static Minecraft getMinecraftInstance() {
        if(instance == null) {
            try {
                ThreadGroup threadgroup = Thread.currentThread().getThreadGroup();
                int i = threadgroup.activeCount();
                Thread athread[] = new Thread[i];
                threadgroup.enumerate(athread);
                for(int j = 0; j < athread.length; j++) {
                    if(!athread[j].getName().equals("Minecraft main thread")) {
                        continue;
                    }
                    instance = (Minecraft)getPrivateValue(java.lang.Thread.class, athread[j], "target");
                    break;
                }

            }
            catch(SecurityException securityexception) {
                throw new RuntimeException(securityexception);
            } catch(NoSuchFieldException nosuchfieldexception) {
                throw new RuntimeException(nosuchfieldexception);
            }
        }
        return instance;
    }

    public static Object getPrivateValue(Class class1, Object obj, String s) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
        try {
            Field field = class1.getDeclaredField(s);
            field.setAccessible(true);
            return field.get(obj);
        } catch(IllegalAccessException illegalaccessexception) {
            ThrowException("An impossible error has occured!", illegalaccessexception);
            return null;
        }
    }

    public static void ThrowException(String s, Throwable throwable)
    {
        Minecraft minecraft = getMinecraftInstance();
        if(minecraft != null) {
            minecraft.showGameStartupError(new GameStartupError(s, throwable));
        } else {
            throw new RuntimeException(throwable);
        }
    }

}