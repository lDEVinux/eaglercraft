package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class MapGenNetherBridge extends MapGenStructure {
	private List spawnList = new ArrayList();

	public MapGenNetherBridge() {
		this.spawnList.add(new SpawnListEntry((w) -> new EntityBlaze(w), 10, 2, 3));
		this.spawnList.add(new SpawnListEntry((w) -> new EntityPigZombie(w), 5, 4, 4));
		this.spawnList.add(new SpawnListEntry((w) -> new EntitySkeleton(w), 10, 4, 4));
		this.spawnList.add(new SpawnListEntry((w) -> new EntityMagmaCube(w), 3, 4, 4));
	}

	public List getSpawnList() {
		return this.spawnList;
	}

	protected boolean canSpawnStructureAtCoords(int par1, int par2) {
		int var3 = par1 >> 4;
		int var4 = par2 >> 4;
		this.rand.setSeed((long) (var3 ^ var4 << 4) ^ this.worldObj.getSeed());
		this.rand.nextInt();
		return this.rand.nextInt(3) != 0 ? false
				: (par1 != (var3 << 4) + 4 + this.rand.nextInt(8) ? false
						: par2 == (var4 << 4) + 4 + this.rand.nextInt(8));
	}

	protected StructureStart getStructureStart(int par1, int par2) {
		return new StructureNetherBridgeStart(this.worldObj, this.rand, par1, par2);
	}
}
