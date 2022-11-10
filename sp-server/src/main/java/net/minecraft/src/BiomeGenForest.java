package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BiomeGenForest extends BiomeGenBase {
	public BiomeGenForest(int par1) {
		super(par1);
		this.spawnableCreatureList.add(new SpawnListEntry((w) -> new EntityWolf(w), 5, 4, 4));
		this.theBiomeDecorator.treesPerChunk = 10;
		this.theBiomeDecorator.grassPerChunk = 2;
	}

	/**
	 * Gets a WorldGen appropriate for this biome.
	 */
	public WorldGenerator getRandomWorldGenForTrees(EaglercraftRandom par1Random) {
		return (WorldGenerator) (par1Random.nextInt(5) == 0 ? this.worldGeneratorForest
				: (par1Random.nextInt(10) == 0 ? this.worldGeneratorBigTree : this.worldGeneratorTrees));
	}
}
