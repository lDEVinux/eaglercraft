package net.minecraft.src;

public class BiomeGenMushroomIsland extends BiomeGenBase {
	public BiomeGenMushroomIsland(int par1) {
		super(par1);
		this.theBiomeDecorator.treesPerChunk = -100;
		this.theBiomeDecorator.flowersPerChunk = -100;
		this.theBiomeDecorator.grassPerChunk = -100;
		this.theBiomeDecorator.mushroomsPerChunk = 1;
		this.theBiomeDecorator.bigMushroomsPerChunk = 1;
		this.topBlock = (byte) Block.mycelium.blockID;
		this.spawnableMonsterList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCreatureList.add(new SpawnListEntry((w) -> new EntityMooshroom(w), 8, 4, 8));
	}
}
