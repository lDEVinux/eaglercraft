package net.minecraft.src;

class InventoryRepair extends InventoryBasic {
	/** Container of this anvil's block. */
	final ContainerRepair theContainer;

	InventoryRepair(ContainerRepair par1ContainerRepair, String par2Str, boolean par3, int par4) {
		super(par2Str, par3, par4);
		this.theContainer = par1ContainerRepair;
	}

	/**
	 * Called when an the contents of an Inventory change, usually
	 */
	public void onInventoryChanged() {
		super.onInventoryChanged();
		this.theContainer.onCraftMatrixChanged(this);
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot.
	 */
	public boolean isStackValidForSlot(int par1, ItemStack par2ItemStack) {
		return true;
	}
}
