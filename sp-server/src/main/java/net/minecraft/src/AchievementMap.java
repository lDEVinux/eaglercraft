package net.minecraft.src;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AchievementMap {
	/** Holds the singleton instance of AchievementMap. */
	public static AchievementMap instance = null;
	
	public static void init(List<String> guid) {
		instance = new AchievementMap(guid);
		StatList.initAll();
	}

	/** Maps a achievement id with it's unique GUID. */
	private Map guidMap = new HashMap();

	private AchievementMap(List<String> guid) {
		for (String var2 : guid) {
			String[] var3 = var2.split(",");
			int var4 = Integer.parseInt(var3[0]);
			this.guidMap.put(Integer.valueOf(var4), var3[1]);
		}
	}

	/**
	 * Returns the unique GUID of a achievement id.
	 */
	public static String getGuid(int par0) {
		return (String) instance.guidMap.get(Integer.valueOf(par0));
	}
}
