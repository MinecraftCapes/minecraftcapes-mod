package net.minecraftcapes.compatibility;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

public class CompatHooks {

    @Getter
    protected static final List<ICompatHooks> hooks = Lists.newArrayList();

    public static void addHook(ICompatHooks compatHooks) {
        hooks.add(compatHooks);
    }

}
