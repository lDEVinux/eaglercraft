package net.minecraft.src;

import java.util.regex.Pattern;

public class StringUtils {
	private static final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");

	public static String stripControlCodes(String par0Str) {
		return patternControlCode.matcher(par0Str).replaceAll("");
	}
}
