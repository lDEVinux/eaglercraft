package net.minecraft.src;

public class BiomeGenBeach extends BiomeGenBase {
	public BiomeGenBeach(int par1) {
		super(par1);
		this.spawnableCreatureList.clear();
		this.topBlock = (byte) Block.sand.blockID;
		this.fillerBlock = (byte) Block.sand.blockID;
		this.theBiomeDecorator.treesPerChunk = -999;
		this.theBiomeDecorator.deadBushPerChunk = 0;
		this.theBiomeDecorator.reedsPerChunk = 0;
		this.theBiomeDecorator.cactiPerChunk = 0;
	}
}
