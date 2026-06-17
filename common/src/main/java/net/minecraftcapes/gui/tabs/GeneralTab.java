package net.minecraftcapes.gui.tabs;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftcapes.player.PlayerHandler;

public class GeneralTab extends GridLayoutTab {
    
    public GeneralTab(Screen parent, Player player) {
        super(Component.translatable("gui.minecraftcapes.tab.general"));
        
        this.layout.defaultCellSetting().padding(100, 4, 4, 0);
        GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
        
        // Open MinecraftCapes
        Button openMinecraftCapes = Button.builder(
                        Component.translatable("button.minecraftcapes.minecraftcapes"),
                        ConfirmLinkScreen.confirmLink(parent, "https://minecraftcapes.net")
                )
                .build();
        
        // Reload Profile
        Button reloadProfile = Button.builder(
                        Component.translatable("button.minecraftcapes.reload"),
                        _ -> PlayerHandler.remove(player.getUUID())
                )
                .build();
        
        Button reloadAllProfiles = Button.builder(
                        Component.translatable("button.minecraftcapes.reload_all"),
                        _ -> PlayerHandler.clearAll()
                )
                .build();
        
        // Tool Tips
        openMinecraftCapes.setTooltip(Tooltip.create(Component.translatable("button.minecraftcapes.minecraftcapes.tooltip")));
        reloadProfile.setTooltip(Tooltip.create(Component.translatable("button.minecraftcapes.reload.tooltip")));
        reloadAllProfiles.setTooltip(Tooltip.create(Component.translatable("button.minecraftcapes.reload_all.tooltip")));
        
        // Add Buttons
        helper.addChild(openMinecraftCapes);
        helper.addChild(reloadProfile);
        helper.addChild(reloadAllProfiles);
    }
}