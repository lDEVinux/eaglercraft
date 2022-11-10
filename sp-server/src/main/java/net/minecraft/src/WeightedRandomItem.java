package net.minecraft.src;

public class WeightedRandomItem {
	/**
	 * The Weight is how often the item is chosen(higher number is higher
	 * chance(lower is lower))
	 */
	protected int itemWeight;

	public WeightedRandomItem(int par1) {
		this.itemWeight = par1;
	}
}
