package net.lax1dude.eaglercraft;

import javax.swing.JOptionPane;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ServerList;

public class MinecraftMain {

	public static void main(String[] par0ArrayOfStr) {
		
		JOptionPane.showMessageDialog(null, "launch renderdoc (optionally) and press ok to continue", "eaglercraft", JOptionPane.PLAIN_MESSAGE);
		
		EaglerAdapter.initializeContext();
		LocalStorageManager.loadStorage();
		
		byte[] b = EaglerAdapter.loadLocalStorage("forced");
		if(b != null) {
			ServerList.loadDefaultServers(Base64.encodeBase64String(b));
		}
		if(par0ArrayOfStr.length > 0) {
			EaglerAdapter.setServerToJoinOnLaunch(par0ArrayOfStr[0]);
		}
		
		Minecraft mc = new Minecraft();
		mc.run();
		
	}
}
