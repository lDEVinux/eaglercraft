package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class EnchantmentNameParts {
	/** The static instance of this class. */
	public static final EnchantmentNameParts instance = new EnchantmentNameParts();

	/** The RNG used to generate enchant names. */
	private EaglercraftRandom rand = new EaglercraftRandom();

	/** List of words used to generate an enchant name. */
	private String[] wordList = "the elder scrolls klaatu berata niktu xyzzy bless curse light darkness fire air earth water hot dry cold wet ignite snuff embiggen twist shorten stretch fiddle destroy imbue galvanize enchant free limited range of towards inside sphere cube self other ball mental physical grow shrink demon elemental spirit animal creature beast humanoid undead fresh stale "
			.split(" ");

	/**
	 * Generates a random enchant name.
	 */
	public String generateRandomEnchantName() {
		int var1 = this.rand.nextInt(2) + 3;
		String var2 = "";

		for (int var3 = 0; var3 < var1; ++var3) {
			if (var3 > 0) {
				var2 = var2 + " ";
			}

			var2 = var2 + this.wordList[this.rand.nextInt(this.wordList.length)];
		}

		return var2;
	}

	/**
	 * Sets the seed for the enchant name RNG.
	 */
	public void setRandSeed(long par1) {
		this.rand.setSeed(par1);
	}
}
