package net.minecraft.src;

import java.util.regex.Pattern;

public class StringUtils {
	private static final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");

	/**
	 * Returns the time elapsed for the given number of ticks, in "mm:ss" format.
	 */
	public static String ticksToElapsedTime(int par0) {
		int var1 = par0 / 20;
		int var2 = var1 / 60;
		var1 %= 60;
		return var1 < 10 ? var2 + ":0" + var1 : var2 + ":" + var1;
	}

	public static String stripControlCodes(String par0Str) {
		return patternControlCode.matcher(par0Str).replaceAll("");
	}
}
