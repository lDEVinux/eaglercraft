package net.minecraft.src;

public class WorldType {
	/** List of world types. */
	public static final WorldType[] worldTypes = new WorldType[16];

	/** Default world type. */
	public static final WorldType DEFAULT = (new WorldType(0, "default", 1)).setVersioned();

	/** Flat world type. */
	public static final WorldType FLAT = new WorldType(1, "flat");

	/** Large Biome world Type. */
	public static final WorldType LARGE_BIOMES = new WorldType(2, "largeBiomes");

	/** Default (1.1) world type. */
	public static final WorldType DEFAULT_1_1 = (new WorldType(8, "default_1_1", 0)).setCanBeCreated(false);

	/** ID for this world type. */
	private final int worldTypeId;
	private final String worldType;

	/** The int version of the ChunkProvider that generated this world. */
	private final int generatorVersion;

	/**
	 * Whether this world type can be generated. Normally true; set to false for
	 * out-of-date generator versions.
	 */
	private boolean canBeCreated;

	/** Whether this WorldType has a version or not. */
	private boolean isWorldTypeVersioned;

	private WorldType(int par1, String par2Str) {
		this(par1, par2Str, 0);
	}

	private WorldType(int par1, String par2Str, int par3) {
		this.worldType = par2Str;
		this.generatorVersion = par3;
		this.canBeCreated = true;
		this.worldTypeId = par1;
		worldTypes[par1] = this;
	}

	public String getWorldTypeName() {
		return this.worldType;
	}

	/**
	 * Returns generatorVersion.
	 */
	public int getGeneratorVersion() {
		return this.generatorVersion;
	}

	public WorldType getWorldTypeForGeneratorVersion(int par1) {
		return this == DEFAULT && par1 == 0 ? DEFAULT_1_1 : this;
	}

	/**
	 * Sets canBeCreated to the provided value, and returns this.
	 */
	private WorldType setCanBeCreated(boolean par1) {
		this.canBeCreated = par1;
		return this;
	}

	/**
	 * Flags this world type as having an associated version.
	 */
	private WorldType setVersioned() {
		this.isWorldTypeVersioned = true;
		return this;
	}

	/**
	 * Returns true if this world Type has a version associated with it.
	 */
	public boolean isVersioned() {
		return this.isWorldTypeVersioned;
	}

	public static WorldType parseWorldType(String par0Str) {
		for (int var1 = 0; var1 < worldTypes.length; ++var1) {
			if (worldTypes[var1] != null && worldTypes[var1].worldType.equalsIgnoreCase(par0Str)) {
				return worldTypes[var1];
			}
		}

		return null;
	}

	public int getWorldTypeID() {
		return this.worldTypeId;
	}
}
