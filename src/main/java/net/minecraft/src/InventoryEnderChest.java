package net.minecraft.src;

public class InventoryEnderChest extends InventoryBasic {
	private TileEntityEnderChest associatedChest;

	public InventoryEnderChest() {
		super("container.enderchest", false, 27);
	}

	public void setAssociatedChest(TileEntityEnderChest par1TileEntityEnderChest) {
		this.associatedChest = par1TileEntityEnderChest;
	}

	public void loadInventoryFromNBT(NBTTagList par1NBTTagList) {
		int var2;

		for (var2 = 0; var2 < this.getSizeInventory(); ++var2) {
			this.setInventorySlotContents(var2, (ItemStack) null);
		}

		for (var2 = 0; var2 < par1NBTTagList.tagCount(); ++var2) {
			NBTTagCompound var3 = (NBTTagCompound) par1NBTTagList.tagAt(var2);
			int var4 = var3.getByte("Slot") & 255;

			if (var4 >= 0 && var4 < this.getSizeInventory()) {
				this.setInventorySlotContents(var4, ItemStack.loadItemStackFromNBT(var3));
			}
		}
	}

	public NBTTagList saveInventoryToNBT() {
		NBTTagList var1 = new NBTTagList("EnderItems");

		for (int var2 = 0; var2 < this.getSizeInventory(); ++var2) {
			ItemStack var3 = this.getStackInSlot(var2);

			if (var3 != null) {
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var2);
				var3.writeToNBT(var4);
				var1.appendTag(var4);
			}
		}

		return var1;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes with
	 * Container
	 */
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
		return this.associatedChest != null && !this.associatedChest.isUseableByPlayer(par1EntityPlayer) ? false : super.isUseableByPlayer(par1EntityPlayer);
	}

	public void openChest() {
		if (this.associatedChest != null) {
			this.associatedChest.openChest();
		}

		super.openChest();
	}

	public void closeChest() {
		if (this.associatedChest != null) {
			this.associatedChest.closeChest();
		}

		super.closeChest();
		this.associatedChest = null;
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot.
	 */
	public boolean isStackValidForSlot(int par1, ItemStack par2ItemStack) {
		return true;
	}
}
