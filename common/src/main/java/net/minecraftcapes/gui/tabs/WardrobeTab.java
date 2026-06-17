package net.minecraftcapes.gui.tabs;

import com.google.gson.Gson;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.LoadingDotsWidget;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftcapes.gui.components.CapeDisplayWidget;
import net.minecraftcapes.utils.WebUtils;
import org.jspecify.annotations.NonNull;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class WardrobeTab implements Tab {
    
    protected final LinearLayout layout = LinearLayout.vertical();
    private LoadingDotsWidget loadingDotsWidget;
    private CycleButton<ButtonStates> capeType;
    private ButtonStates selectedType = ButtonStates.JAVA;
    private OfficialCapes officialCape = null;
    private enum ButtonStates {
        JAVA,
        BEDROCK,
        LCE,
        MINECON,
        UNUSED
    }
    
    public WardrobeTab(Screen parent, Player player) {
        this.layout.defaultCellSetting().alignVerticallyTop().alignHorizontallyCenter();
        
        capeType = new CycleButton.Builder<>((b) -> Component.nullToEmpty(b.name()), () -> selectedType)
                .withValues(ButtonStates.values())
                .create(
                        Component.nullToEmpty("Cape Type"),
                        (_, value) -> selectedType = value
                );
        
        loadingDotsWidget = new LoadingDotsWidget(parent.getFont(), Component.nullToEmpty("Loading"));
        this.layout.addChild(loadingDotsWidget);
        
        byte[] apiData = WebUtils.get("https://api.minecraftcapes.net/api/gallery/officialcapes");
        if(apiData != null) {
            String json = new String(apiData, StandardCharsets.UTF_8);
            officialCape = new Gson().fromJson(json, OfficialCapes.class);
            
            this.layout.removeChildren();
            this.layout.addChild(capeType);
            
            CapeDisplayWidget temp = new CapeDisplayWidget("woooo");
            this.layout.addChild(temp);
            
            System.out.println(json);
        }
    }
    
    
    @Override
    public @NonNull Component getTabTitle() {
        return Component.translatable("gui.minecraftcapes.tab.wardrobe");
    }
    
    @Override
    public @NonNull Component getTabExtraNarration() {
        return Component.translatable("gui.minecraftcapes.tab.wardrobe");
    }
    
    @Override
    public void visitChildren(@NonNull Consumer<AbstractWidget> consumer) {
        this.layout.visitWidgets(consumer);
    }
    
    @Override
    public void doLayout(@NonNull ScreenRectangle screenRectangle) {
        this.layout.arrangeElements();
        FrameLayout.alignInRectangle(this.layout, screenRectangle, 0.5F, 0.5F);
    }
    
    @Override
    public Layout getLayout() {
        return this.layout;
    }
    
    private class OfficialCapes {
        String name;
    }
}