package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

import net.lax1dude.eaglercraft.EaglerAdapter;

public class AchievementMap {
	/** Holds the singleton instance of AchievementMap. */
	public static AchievementMap instance = new AchievementMap();

	/** Maps a achievement id with it's unique GUID. */
	private Map guidMap = new HashMap();

	private AchievementMap() {
		try {
			for(String str : EaglerAdapter.fileContentsLines("/achievement/map.txt")) {
				String[] var3 = str.split(",");
				int var4 = Integer.parseInt(var3[0]);
				this.guidMap.put(Integer.valueOf(var4), var3[1]);
			}
		} catch (Exception var5) {
			var5.printStackTrace();
		}
	}

	/**
	 * Returns the unique GUID of a achievement id.
	 */
	public static String getGuid(int par0) {
		return (String) instance.guidMap.get(Integer.valueOf(par0));
	}
}
