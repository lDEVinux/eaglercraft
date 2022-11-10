package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class TileEntityDispenser extends TileEntity implements IInventory {
	private ItemStack[] dispenserContents = new ItemStack[9];

	/**
	 * random number generator for instance. Used in random item stack selection.
	 */
	private EaglercraftRandom dispenserRandom = new EaglercraftRandom();
	protected String customName;

	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory() {
		return 9;
	}

	/**
	 * Returns the stack in slot i
	 */
	public ItemStack getStackInSlot(int par1) {
		return this.dispenserContents[par1];
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number (second
	 * arg) of items and returns them in a new stack.
	 */
	public ItemStack decrStackSize(int par1, int par2) {
		if (this.dispenserContents[par1] != null) {
			ItemStack var3;

			if (this.dispenserContents[par1].stackSize <= par2) {
				var3 = this.dispenserContents[par1];
				this.dispenserContents[par1] = null;
				this.onInventoryChanged();
				return var3;
			} else {
				var3 = this.dispenserContents[par1].splitStack(par2);

				if (this.dispenserContents[par1].stackSize == 0) {
					this.dispenserContents[par1] = null;
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
		if (this.dispenserContents[par1] != null) {
			ItemStack var2 = this.dispenserContents[par1];
			this.dispenserContents[par1] = null;
			return var2;
		} else {
			return null;
		}
	}

	public int getRandomStackFromInventory() {
		int var1 = -1;
		int var2 = 1;

		for (int var3 = 0; var3 < this.dispenserContents.length; ++var3) {
			if (this.dispenserContents[var3] != null && this.dispenserRandom.nextInt(var2++) == 0) {
				var1 = var3;
			}
		}

		return var1;
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
		this.dispenserContents[par1] = par2ItemStack;

		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
	}

	/**
	 * Add item stack in first available inventory slot
	 */
	public int addItem(ItemStack par1ItemStack) {
		for (int var2 = 0; var2 < this.dispenserContents.length; ++var2) {
			if (this.dispenserContents[var2] == null || this.dispenserContents[var2].itemID == 0) {
				this.setInventorySlotContents(var2, par1ItemStack);
				return var2;
			}
		}

		return -1;
	}

	/**
	 * Returns the name of the inventory.
	 */
	public String getInvName() {
		return this.isInvNameLocalized() ? this.customName : "container.dispenser";
	}

	public void setCustomName(String par1Str) {
		this.customName = par1Str;
	}

	/**
	 * If this returns false, the inventory name will be used as an unlocalized
	 * name, and translated into the player's language. Otherwise it will be used
	 * directly.
	 */
	public boolean isInvNameLocalized() {
		return this.customName != null;
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.dispenserContents = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
			NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
			int var5 = var4.getByte("Slot") & 255;

			if (var5 >= 0 && var5 < this.dispenserContents.length) {
				this.dispenserContents[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		if (par1NBTTagCompound.hasKey("CustomName")) {
			this.customName = par1NBTTagCompound.getString("CustomName");
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.dispenserContents.length; ++var3) {
			if (this.dispenserContents[var3] != null) {
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.dispenserContents[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		par1NBTTagCompound.setTag("Items", var2);

		if (this.isInvNameLocalized()) {
			par1NBTTagCompound.setString("CustomName", this.customName);
		}
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64,
	 * possibly will be extended. *Isn't this more of a set than a get?*
	 */
	public int getInventoryStackLimit() {
		return 64;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes with
	 * Container
	 */
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
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
