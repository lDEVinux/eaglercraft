package net.minecraft.src;

public class WorldProviderEnd extends WorldProvider {
	/**
	 * creates a new world chunk manager for WorldProvider
	 */
	public void registerWorldChunkManager() {
		this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.5F, 0.0F);
		this.dimensionId = 1;
		this.hasNoSky = true;
	}

	/**
	 * Returns a new chunk provider which generates chunks for this world
	 */
	public IChunkProvider createChunkGenerator() {
		return new ChunkProviderEnd(this.worldObj, this.worldObj.getSeed());
	}

	/**
	 * Calculates the angle of sun and moon in the sky relative to a specified time
	 * (usually worldTime)
	 */
	public float calculateCelestialAngle(long par1, float par3) {
		return 0.0F;
	}

	/**
	 * True if the player can respawn in this dimension (true = overworld, false =
	 * nether).
	 */
	public boolean canRespawnHere() {
		return false;
	}

	/**
	 * Returns 'true' if in the "main surface world", but 'false' if in the Nether
	 * or End dimensions.
	 */
	public boolean isSurfaceWorld() {
		return false;
	}

	/**
	 * Will check if the x, z position specified is alright to be set as the map
	 * spawn point
	 */
	public boolean canCoordinateBeSpawn(int par1, int par2) {
		int var3 = this.worldObj.getFirstUncoveredBlock(par1, par2);
		return var3 == 0 ? false : Block.blocksList[var3].blockMaterial.blocksMovement();
	}

	/**
	 * Gets the hard-coded portal location to use when entering this dimension.
	 */
	public ChunkCoordinates getEntrancePortalLocation() {
		return new ChunkCoordinates(100, 50, 0);
	}

	public int getAverageGroundLevel() {
		return 50;
	}

	/**
	 * Returns the dimension's name, e.g. "The End", "Nether", or "Overworld".
	 */
	public String getDimensionName() {
		return "The End";
	}
}
