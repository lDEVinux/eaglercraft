package net.minecraft.src;

public class BiomeGenPlains extends BiomeGenBase {
	protected BiomeGenPlains(int par1) {
		super(par1);
		this.theBiomeDecorator.treesPerChunk = -999;
		this.theBiomeDecorator.flowersPerChunk = 4;
		this.theBiomeDecorator.grassPerChunk = 10;
	}
}
