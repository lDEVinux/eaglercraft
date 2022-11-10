package net.minecraft.src;

public interface IBehaviorDispenseItem {

	/**
	 * Dispenses the specified ItemStack from a dispenser.
	 */
	ItemStack dispense(IBlockSource var1, ItemStack var2);
}
