package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BiomeGenTaiga extends BiomeGenBase {
	public BiomeGenTaiga(int par1) {
		super(par1);
		this.spawnableCreatureList.add(new SpawnListEntry((w) -> new EntityWolf(w), 8, 4, 4));
		this.theBiomeDecorator.treesPerChunk = 10;
		this.theBiomeDecorator.grassPerChunk = 1;
	}

	/**
	 * Gets a WorldGen appropriate for this biome.
	 */
	public WorldGenerator getRandomWorldGenForTrees(EaglercraftRandom par1Random) {
		return (WorldGenerator) (par1Random.nextInt(3) == 0 ? new WorldGenTaiga1() : new WorldGenTaiga2(false));
	}
}
