package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BiomeGenDesert extends BiomeGenBase {
	public BiomeGenDesert(int par1) {
		super(par1);
		this.spawnableCreatureList.clear();
		this.topBlock = (byte) Block.sand.blockID;
		this.fillerBlock = (byte) Block.sand.blockID;
		this.theBiomeDecorator.treesPerChunk = -999;
		this.theBiomeDecorator.deadBushPerChunk = 2;
		this.theBiomeDecorator.reedsPerChunk = 50;
		this.theBiomeDecorator.cactiPerChunk = 10;
	}

	public void decorate(World par1World, EaglercraftRandom par2Random, int par3, int par4) {
		super.decorate(par1World, par2Random, par3, par4);

		if (par2Random.nextInt(1000) == 0) {
			int var5 = par3 + par2Random.nextInt(16) + 8;
			int var6 = par4 + par2Random.nextInt(16) + 8;
			WorldGenDesertWells var7 = new WorldGenDesertWells();
			var7.generate(par1World, par2Random, var5, par1World.getHeightValue(var5, var6) + 1, var6);
		}
	}
}
