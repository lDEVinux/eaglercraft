// 
// Decompiled by Procyon v0.5.36
// 

package net.md_5.bungee.api;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public enum ChatColor {
	BLACK('0'), DARK_BLUE('1'), DARK_GREEN('2'), DARK_AQUA('3'), DARK_RED('4'), DARK_PURPLE('5'), GOLD('6'), GRAY('7'), DARK_GRAY('8'), BLUE('9'), GREEN('a'), AQUA('b'), RED('c'), LIGHT_PURPLE('d'), YELLOW('e'), WHITE('f'), MAGIC('k'),
	BOLD('l'), STRIKETHROUGH('m'), UNDERLINE('n'), ITALIC('o'), RESET('r');

	public static final char COLOR_CHAR = '\u00A7';
	private static final Pattern STRIP_COLOR_PATTERN;
	private static final Map<Character, ChatColor> BY_CHAR;
	private final char code;
	private final String toString;

	private ChatColor(final char code) {
		this.code = code;
		this.toString = new String(new char[] { '\u00A7', code });
	}

	@Override
	public String toString() {
		return this.toString;
	}

	public static String stripColor(final String input) {
		if (input == null) {
			return null;
		}
		return ChatColor.STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
	}

	public static String translateAlternateColorCodes(final char altColorChar, final String textToTranslate) {
		final char[] b = textToTranslate.toCharArray();
		for (int i = 0; i < b.length - 1; ++i) {
			if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
				b[i] = '\u00A7';
				b[i + 1] = Character.toLowerCase(b[i + 1]);
			}
		}
		return new String(b);
	}

	public static ChatColor getByChar(final char code) {
		return ChatColor.BY_CHAR.get(code);
	}

	static {
		STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('\u00A7') + "[0-9A-FK-OR]");
		BY_CHAR = new HashMap<Character, ChatColor>();
		for (final ChatColor colour : values()) {
			ChatColor.BY_CHAR.put(colour.code, colour);
		}
	}
}
