package net.lax1dude.eaglercraft;

import java.io.IOException;

import net.minecraft.src.Achievement;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.NBTTagCompound;

public class LocalStorageManager {

	public static NBTTagCompound achievementStorage = null;
	public static NBTTagCompound gameSettingsStorage = null;
	public static NBTTagCompound profileSettingsStorage = null;
	
	public static void loadStorage() {
		byte[] a = EaglerAdapter.loadLocalStorage("a");
		byte[] g = EaglerAdapter.loadLocalStorage("g");
		byte[] p = EaglerAdapter.loadLocalStorage("p");
		
		if(a != null) {
			try {
				achievementStorage = CompressedStreamTools.readUncompressed(a);
			}catch(IOException e) {
				;
			}
		}
		
		if(g != null) {
			try {
				gameSettingsStorage = CompressedStreamTools.readUncompressed(g);
			}catch(IOException e) {
				;
			}
		}
		
		if(p != null) {
			try {
				profileSettingsStorage = CompressedStreamTools.readUncompressed(p);
			}catch(IOException e) {
				;
			}
		}

		if(achievementStorage == null) achievementStorage = new NBTTagCompound();
		if(gameSettingsStorage == null) gameSettingsStorage = new NBTTagCompound();
		if(profileSettingsStorage == null) profileSettingsStorage = new NBTTagCompound();
		
	}
	
	public static void saveStorageA() {
		try {
			EaglerAdapter.saveLocalStorage("a", CompressedStreamTools.writeUncompressed(achievementStorage));
		} catch (IOException e) {
			;
		}
	}
	
	public static void saveStorageG() {
		try {
			EaglerAdapter.saveLocalStorage("g", CompressedStreamTools.writeUncompressed(gameSettingsStorage));
		} catch (IOException e) {
			;
		}
	}
	
	public static void saveStorageP() {
		try {
			EaglerAdapter.saveLocalStorage("p", CompressedStreamTools.writeUncompressed(profileSettingsStorage));
		} catch (IOException e) {
			;
		}
	}
	
	public static String dumpConfiguration() {
		try {
			return Base64.encodeBase64String(CompressedStreamTools.writeUncompressed(gameSettingsStorage));
		} catch(Throwable e) {
			return "<error>";
		}
	}

	public static boolean hasMadeAchievement(Achievement stat) {
		if(stat.parentAchievement != null && (!achievementStorage.getBoolean(stat.parentAchievement.statGuid))) {
			return false;
		}else {
			if(!achievementStorage.getBoolean(stat.statGuid)) {
				achievementStorage.setBoolean(stat.statGuid, true);
				saveStorageA();
				return true;
			}else {
				return false;
			}
		}
	}

}
