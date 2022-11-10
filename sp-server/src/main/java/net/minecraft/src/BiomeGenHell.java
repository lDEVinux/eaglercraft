package net.minecraft.src;

public class BiomeGenHell extends BiomeGenBase {
	public BiomeGenHell(int par1) {
		super(par1);
		this.spawnableMonsterList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCaveCreatureList.clear();
		this.spawnableMonsterList.add(new SpawnListEntry((w) -> new EntityGhast(w), 50, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry((w) -> new EntityPigZombie(w), 100, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry((w) -> new EntityMagmaCube(w), 1, 4, 4));
	}
}
