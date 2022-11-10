package net.minecraft.src;

public class ChatAllowedCharacters {
	/**
	 * This String have the characters allowed in any text drawing of minecraft.
	 */
	public static final String allowedCharacters = getAllowedCharacters();

	/**
	 * Array of the special characters that are allowed in any text drawing of
	 * Minecraft.
	 */
	public static final char[] allowedCharactersArray = new char[] { '/', '\n', '\r', '\t', '\u0000', '\f', '`', '?',
			'*', '\\', '<', '>', '|', '\"', ':' };

	/**
	 * Load the font.txt resource file, that is on UTF-8 format. This file contains
	 * the characters that minecraft can render Strings on screen.
	 */
	private static String getAllowedCharacters() {
		int[] codePoints = new int[]{
			32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,
			48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,
			64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,
			80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,
			39,97,98,99,100,101,102,103,104,105,106,107,108,
			109,110,111,112,113,114,115,116,117,118,119,120,
			121,122,123,124,125,126,8962,199,252,233,226,228,
			224,229,231,234,235,232,239,238,236,196,197,201,
			230,198,244,246,242,251,249,255,214,220,248,163,
			216,215,402,225,237,243,250,241,209,170,186,191,
			174,172,189,188,161,171,187
		};
		char[] chars = new char[codePoints.length];
		for(int i = 0; i < codePoints.length; ++i) {
			chars[i] = (char)codePoints[i];
		}
		return new String(chars);
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
