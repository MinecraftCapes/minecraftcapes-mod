package net.minecraftcapes.gui.tabs;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class LinksTab extends GridLayoutTab {
    
    public LinksTab(Screen parent) {
        super(Component.translatable("gui.minecraftcapes.tab.links"));
        
        this.layout.defaultCellSetting().padding(100, 4, 4, 0);
        GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
        
        // MinecraftCapes
        Button openMinecraftCapes = Button.builder(
                        Component.translatable("button.minecraftcapes.minecraftcapes"),
                        ConfirmLinkScreen.confirmLink(parent, "https://minecraftcapes.net")
                )
                .build();
        
        Button openDiscord = Button.builder(
                        Component.translatable("button.minecraftcapes.discord"),
                        ConfirmLinkScreen.confirmLink(parent, "https://discord.gg/minecraftcapes")
                )
                .build();
        
        Button openGitHub = Button.builder(
                        Component.translatable("button.minecraftcapes.github"),
                        ConfirmLinkScreen.confirmLink(parent, "https://github.com/minecraftcapes")
                )
                .build();
        
        // Add Buttons
        helper.addChild(openMinecraftCapes);
        helper.addChild(openDiscord);
        helper.addChild(openGitHub);
    }
}