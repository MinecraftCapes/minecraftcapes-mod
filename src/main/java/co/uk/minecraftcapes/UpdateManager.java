package co.uk.minecraftcapes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class UpdateManager {
	
	static boolean updateChecked = false;
	static boolean isUpdate = false;
	
	public static void getUpdate() {				
		if(!updateChecked || isUpdate) {
			updateChecked = true;
			if((checkUpdate() || isUpdate) && Minecraft.getMinecraft().thePlayer != null) {
				isUpdate = true;
				EntityPlayer p = Minecraft.getMinecraft().thePlayer;
				ITextComponent msg = new TextComponentString("§e[MinecraftCapes]§r An update is available! Please download the latest version");
				p.addChatMessage(msg);
			}
		}
	}
	
	private static boolean checkUpdate() {		
		JsonParser jp = new JsonParser();		
		JsonObject rootobj;
		try {
			String currentVersion = readJsonFromUrl("https://minecraftcapes.co.uk/api/getversion").getAsString();			
			return !currentVersion.equals(Reference.VERSION);			
		} catch(Exception e) {			
		}
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
