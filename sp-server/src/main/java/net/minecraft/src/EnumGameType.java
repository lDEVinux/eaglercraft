package net.minecraft.src;

public enum EnumGameType {
	NOT_SET(-1, ""), SURVIVAL(0, "survival"), CREATIVE(1, "creative"), ADVENTURE(2, "adventure");

	int id;
	String name;

	private EnumGameType(int par3, String par4Str) {
		this.id = par3;
		this.name = par4Str;
	}

	/**
	 * Returns the ID of this game type
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Returns the name of this game type
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Configures the player capabilities based on the game type
	 */
	public void configurePlayerCapabilities(PlayerCapabilities par1PlayerCapabilities) {
		if (this == CREATIVE) {
			par1PlayerCapabilities.allowFlying = true;
			par1PlayerCapabilities.isCreativeMode = true;
			par1PlayerCapabilities.disableDamage = true;
		} else {
			par1PlayerCapabilities.allowFlying = false;
			par1PlayerCapabilities.isCreativeMode = false;
			par1PlayerCapabilities.disableDamage = false;
			par1PlayerCapabilities.isFlying = false;
		}

		par1PlayerCapabilities.allowEdit = !this.isAdventure();
	}

	/**
	 * Returns true if this is the ADVENTURE game type
	 */
	public boolean isAdventure() {
		return this == ADVENTURE;
	}

	/**
	 * Returns true if this is the CREATIVE game type
	 */
	public boolean isCreative() {
		return this == CREATIVE;
	}

	/**
	 * Returns the game type with the specified ID, or SURVIVAL if none found. Args:
	 * id
	 */
	public static EnumGameType getByID(int par0) {
		EnumGameType[] var1 = values();
		int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			EnumGameType var4 = var1[var3];

			if (var4.id == par0) {
				return var4;
			}
		}

		return SURVIVAL;
	}
}
