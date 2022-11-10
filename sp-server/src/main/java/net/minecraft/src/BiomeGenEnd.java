package net.minecraft.src;

public class BiomeGenEnd extends BiomeGenBase {
	public BiomeGenEnd(int par1) {
		super(par1);
		this.spawnableMonsterList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCaveCreatureList.clear();
		this.spawnableMonsterList.add(new SpawnListEntry((w) -> new EntityEnderman(w), 10, 4, 4));
		this.topBlock = (byte) Block.dirt.blockID;
		this.fillerBlock = (byte) Block.dirt.blockID;
		this.theBiomeDecorator = new BiomeEndDecorator(this);
	}
}
