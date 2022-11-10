package net.minecraft.src;

public interface IMob extends IAnimals {
	/** Entity selector for IMob types. */
	IEntitySelector mobSelector = new FilterIMob();
}
