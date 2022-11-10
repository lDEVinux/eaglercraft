package net.minecraft.src;

import java.util.List;

public class InventoryBasic implements IInventory {
	private String inventoryTitle;
	private int slotsCount;
	private ItemStack[] inventoryContents;
	private List field_70480_d;
	private boolean field_94051_e;

	public InventoryBasic(String par1Str, boolean par2, int par3) {
		this.inventoryTitle = par1Str;
		this.field_94051_e = par2;
		this.slotsCount = par3;
		this.inventoryContents = new ItemStack[par3];
	}

	/**
	 * Returns the stack in slot i
	 */
	public ItemStack getStackInSlot(int par1) {
		return this.inventoryContents[par1];
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number (second
	 * arg) of items and returns them in a new stack.
	 */
	public ItemStack decrStackSize(int par1, int par2) {
		if (this.inventoryContents[par1] != null) {
			ItemStack var3;

			if (this.inventoryContents[par1].stackSize <= par2) {
				var3 = this.inventoryContents[par1];
				this.inventoryContents[par1] = null;
				this.onInventoryChanged();
				return var3;
			} else {
				var3 = this.inventoryContents[par1].splitStack(par2);

				if (this.inventoryContents[par1].stackSize == 0) {
					this.inventoryContents[par1] = null;
				}

				this.onInventoryChanged();
				return var3;
			}
		} else {
			return null;
		}
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench GUI.
	 */
	public ItemStack getStackInSlotOnClosing(int par1) {
		if (this.inventoryContents[par1] != null) {
			ItemStack var2 = this.inventoryContents[par1];
			this.inventoryContents[par1] = null;
			return var2;
		} else {
			return null;
		}
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
		this.inventoryContents[par1] = par2ItemStack;

		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory() {
		return this.slotsCount;
	}

	/**
	 * Returns the name of the inventory.
	 */
	public String getInvName() {
		return this.inventoryTitle;
	}

	/**
	 * If this returns false, the inventory name will be used as an unlocalized
	 * name, and translated into the player's language. Otherwise it will be used
	 * directly.
	 */
	public boolean isInvNameLocalized() {
		return this.field_94051_e;
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64,
	 * possibly will be extended. *Isn't this more of a set than a get?*
	 */
	public int getInventoryStackLimit() {
		return 64;
	}

	/**
	 * Called when an the contents of an Inventory change, usually
	 */
	public void onInventoryChanged() {
		if (this.field_70480_d != null) {
			for (int var1 = 0; var1 < this.field_70480_d.size(); ++var1) {
				((IInvBasic) this.field_70480_d.get(var1)).onInventoryChanged(this);
			}
		}
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes with
	 * Container
	 */
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
		return true;
	}

	public void openChest() {
	}

	public void closeChest() {
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot.
	 */
	public boolean isStackValidForSlot(int par1, ItemStack par2ItemStack) {
		return true;
	}
}
