package net.minecraft.src;

public interface ISaveFormat {
	/**
	 * Returns back a loader for the specified save directory
	 */
	ISaveHandler getSaveLoader(String var1, boolean var2);

	void flushCache();

	/**
	 * @args: Takes one argument - the name of the directory of the world to
	 *        delete. @desc: Delete the world by deleting the associated directory
	 *        recursively.
	 */
	boolean deleteWorldDirectory(String var1);

	/**
	 * gets if the map is old chunk saving (true) or McRegion (false)
	 */
	boolean isOldMapFormat(String var1);

	/**
	 * converts the map to mcRegion
	 */
	boolean convertMapFormat(String var1, IProgressUpdate var2);
}
