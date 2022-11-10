package net.minecraft.src;

public interface ISidedInventory extends IInventory {
	/**
	 * param side
	 */
	int[] getSlotsForFace(int var1);

	/**
	 * Returns true if automation can insert the given item in the given slot from
	 * the given side. Args: Slot, item, side
	 */
	boolean canInsertItem(int var1, ItemStack var2, int var3);

	/**
	 * Returns true if automation can extract the given item in the given slot from
	 * the given side. Args: Slot, item, side
	 */
	boolean canExtractItem(int var1, ItemStack var2, int var3);
}
