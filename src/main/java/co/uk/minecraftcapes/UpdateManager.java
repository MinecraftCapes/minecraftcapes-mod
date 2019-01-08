package co.uk.minecraftcapes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

public class UpdateManager {
	
	static boolean updateChecked = false;
	static boolean isUpdate = false;
	
	public static void getUpdate() {				
		if(!updateChecked || isUpdate) {
			updateChecked = true;
			if((checkUpdate() || isUpdate) && Minecraft.getMinecraft().player != null) {
				isUpdate = true;
				EntityPlayer p = Minecraft.getMinecraft().player;				
				ITextComponent prefix = new TextComponentString("§e[MinecraftCapes]§r");
				prefix.getStyle().setColor(TextFormatting.YELLOW);
				ITextComponent msg = new TextComponentString(" An update is available! Click to download the latest version");
				msg.getStyle().setColor(TextFormatting.WHITE);
				msg.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://minecraftcapes.co.uk/download"));
				prefix.appendSibling(msg);				
				p.sendMessage(prefix);
			}
		}
	}
	
	private static boolean checkUpdate() {					
		try {
			String currentVersion = readJsonFromUrl("https://minecraftcapes.co.uk/api/getversion&v=6").getAsString();			
			return !currentVersion.equals(Reference.VERSION);			
		} catch(Exception e) {}
		return false;	
	}
	
	public static JsonElement readJsonFromUrl(String url) throws Exception {
		InputStream is = new URL(url).openStream();	    
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));	      
	    StringBuilder sb = new StringBuilder();
	      
	    int cp;
	    while ((cp = rd.read()) != -1) {
	    	sb.append((char) cp);
	    }
	    String jsonText = sb.toString();
	
	    is.close();
	      
	    return new JsonParser().parse(jsonText);	    
	}
}
