package net.minecraftcapes.gui.tabs;

import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import net.minecraftcapes.config.MinecraftCapesConfig;

public class OptionsTab extends GridLayoutTab {
    
    public OptionsTab() {
        super(Component.translatable("gui.minecraftcapes.tab.options"));
        
        this.layout.defaultCellSetting().padding(100, 4, 4, 0);
        GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
        
        CycleButton<Boolean> customCapes = CycleButton.onOffBuilder(MinecraftCapesConfig.isCapeVisible())
                .create(
                        Component.translatable("button.minecraftcapes.custom_capes"),
                        (_, value) -> MinecraftCapesConfig.setCapeVisible(value)
                );
        
        CycleButton<Boolean> customEars = CycleButton.onOffBuilder(MinecraftCapesConfig.isEarsVisible())
                .create(
                        Component.translatable("button.minecraftcapes.custom_ears"),
                        (_, value) -> MinecraftCapesConfig.setEarsVisible(value)
                );
        
        // Tool Tips
        customCapes.setTooltip(Tooltip.create(Component.translatable("button.minecraftcapes.custom_capes.tooltip")));
        customEars.setTooltip(Tooltip.create(Component.translatable("button.minecraftcapes.custom_ears.tooltip")));
        
        // Add Buttons
        helper.addChild(customCapes);
        helper.addChild(customEars);
    }
}