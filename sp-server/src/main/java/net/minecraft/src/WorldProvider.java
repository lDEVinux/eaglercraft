package net.minecraft.src;

public abstract class WorldProvider {
	/** world object being used */
	public World worldObj;
	public WorldType terrainType;
	public String field_82913_c;

	/** World chunk manager being used to generate chunks */
	public WorldChunkManager worldChunkMgr;

	/**
	 * States whether the Hell world provider is used(true) or if the normal world
	 * provider is used(false)
	 */
	public boolean isHellWorld = false;

	/**
	 * A boolean that tells if a world does not have a sky. Used in calculating
	 * weather and skylight
	 */
	public boolean hasNoSky = false;

	/** Light to brightness conversion table */
	public float[] lightBrightnessTable = new float[16];

	/** The id for the dimension (ex. -1: Nether, 0: Overworld, 1: The End) */
	public int dimensionId = 0;

	/** Array for sunrise/sunset colors (RGBA) */
	private float[] colorsSunriseSunset = new float[4];

	/**
	 * associate an existing world with a World provider, and setup its
	 * lightbrightness table
	 */
	public final void registerWorld(World par1World) {
		this.worldObj = par1World;
		this.terrainType = par1World.getWorldInfo().getTerrainType();
		this.field_82913_c = par1World.getWorldInfo().getGeneratorOptions();
		this.registerWorldChunkManager();
		this.generateLightBrightnessTable();
	}

	/**
	 * Creates the light to brightness table
	 */
	protected void generateLightBrightnessTable() {
		float var1 = 0.0F;

		for (int var2 = 0; var2 <= 15; ++var2) {
			float var3 = 1.0F - (float) var2 / 15.0F;
			this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
		}
	}

	/**
	 * creates a new world chunk manager for WorldProvider
	 */
	protected void registerWorldChunkManager() {
		if (this.worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT) {
			FlatGeneratorInfo var1 = FlatGeneratorInfo
					.createFlatGeneratorFromString(this.worldObj.getWorldInfo().getGeneratorOptions());
			this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.biomeList[var1.getBiome()], 0.5F, 0.5F);
		} else {
			this.worldChunkMgr = new WorldChunkManager(this.worldObj);
		}
	}

	/**
	 * Returns a new chunk provider which generates chunks for this world
	 */
	public IChunkProvider createChunkGenerator() {
		return (IChunkProvider) (this.terrainType == WorldType.FLAT
				? new ChunkProviderFlat(this.worldObj, this.worldObj.getSeed(),
						this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.field_82913_c)
				: new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(),
						this.worldObj.getWorldInfo().isMapFeaturesEnabled()));
	}

	/**
	 * Will check if the x, z position specified is alright to be set as the map
	 * spawn point
	 */
	public boolean canCoordinateBeSpawn(int par1, int par2) {
		int var3 = this.worldObj.getFirstUncoveredBlock(par1, par2);
		return var3 == Block.grass.blockID;
	}

	/**
	 * Calculates the angle of sun and moon in the sky relative to a specified time
	 * (usually worldTime)
	 */
	public float calculateCelestialAngle(long par1, float par3) {
		int var4 = (int) (par1 % 24000L);
		float var5 = ((float) var4 + par3) / 24000.0F - 0.25F;

		if (var5 < 0.0F) {
			++var5;
		}

		if (var5 > 1.0F) {
			--var5;
		}

		float var6 = var5;
		var5 = 1.0F - (float) ((Math.cos((double) var5 * Math.PI) + 1.0D) / 2.0D);
		var5 = var6 + (var5 - var6) / 3.0F;
		return var5;
	}

	public int func_76559_b(long par1) {
		return (int) (par1 / 24000L) % 8;
	}

	/**
	 * Returns 'true' if in the "main surface world", but 'false' if in the Nether
	 * or End dimensions.
	 */
	public boolean isSurfaceWorld() {
		return true;
	}

	/**
	 * True if the player can respawn in this dimension (true = overworld, false =
	 * nether).
	 */
	public boolean canRespawnHere() {
		return true;
	}

	public static WorldProvider getProviderForDimension(int par0) {
		return (WorldProvider) (par0 == -1 ? new WorldProviderHell()
				: (par0 == 0 ? new WorldProviderSurface() : (par0 == 1 ? new WorldProviderEnd() : null)));
	}

	/**
	 * Gets the hard-coded portal location to use when entering this dimension.
	 */
	public ChunkCoordinates getEntrancePortalLocation() {
		return null;
	}

	public int getAverageGroundLevel() {
		return this.terrainType == WorldType.FLAT ? 4 : 64;
	}

	/**
	 * Returns the dimension's name, e.g. "The End", "Nether", or "Overworld".
	 */
	public abstract String getDimensionName();
}
