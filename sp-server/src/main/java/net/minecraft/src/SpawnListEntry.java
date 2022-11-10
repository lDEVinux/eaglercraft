package net.minecraft.src;

import java.util.function.Function;

public class SpawnListEntry extends WeightedRandomItem {
	/** Holds the class of the entity to be spawned. */
	public Function<World, EntityLiving> entityConstructor;
	public int minGroupCount;
	public int maxGroupCount;

	public SpawnListEntry(Function<World, EntityLiving> par1Class, int par2, int par3, int par4) {
		super(par2);
		this.entityConstructor = par1Class;
		this.minGroupCount = par3;
		this.maxGroupCount = par4;
	}
}
