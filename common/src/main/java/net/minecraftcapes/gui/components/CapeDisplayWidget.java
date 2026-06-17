package net.minecraftcapes.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.SelectableEntry;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;

public class CapeDisplayWidget extends ObjectSelectionList.Entry implements SelectableEntry  {
    
    private final StringWidget capeText;
    
    public CapeDisplayWidget(String text) {
        this.capeText = new StringWidget(Component.nullToEmpty(text), Minecraft.getInstance().font);
        
    }
    
    @Override
    public Component getNarration() {
        return Component.nullToEmpty("Woop");
    }
    
    @Override
    public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean b, float a) {
        this.capeText.setPosition(10, this.getContentY() + 30);
        this.capeText.extractRenderState(graphics, mouseX, mouseY, a);
    }
    
}
