package net.minecraft.src;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import net.lax1dude.eaglercraft.EaglerAdapter;

public class ChatAllowedCharacters {
	/**
	 * This String have the characters allowed in any text drawing of minecraft.
	 */
	public static String allowedCharacters = null;

	/**
	 * Array of the special characters that are allowed in any text drawing of
	 * Minecraft.
	 */
	public static final char[] allowedCharactersArray = new char[] { '/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };

	/**
	 * Load the font.txt resource file, that is on UTF-8 format. This file contains
	 * the characters that minecraft can render Strings on screen.
	 */
	public static void getAllowedCharacters() {
		String var0 = "";

		try {
			BufferedReader var1 = new BufferedReader(new InputStreamReader(EaglerAdapter.loadResource("/font.txt"), "UTF-8"));
			String var2 = "";

			while ((var2 = var1.readLine()) != null) {
				if (!var2.startsWith("#")) {
					var0 = var0 + var2;
				}
			}

			var1.close();
		} catch (Exception var3) {
			;
		}

		allowedCharacters = var0;
	}

	public static final boolean isAllowedCharacter(char par0) {
		return par0 != 167 && (allowedCharacters.indexOf(par0) >= 0 || par0 > 32);
	}

	/**
	 * Filter string by only keeping those characters for which isAllowedCharacter()
	 * returns true.
	 */
	public static String filerAllowedCharacters(String par0Str) {
		StringBuilder var1 = new StringBuilder();
		char[] var2 = par0Str.toCharArray();
		int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			char var5 = var2[var4];

			if (isAllowedCharacter(var5)) {
				var1.append(var5);
			}
		}

		return var1.toString();
	}
}
