package net.minecraft.src;

public class TileEntityFurnace extends TileEntity implements ISidedInventory {
	private static final int[] field_102010_d = new int[] { 0 };
	private static final int[] field_102011_e = new int[] { 2, 1 };
	private static final int[] field_102009_f = new int[] { 1 };

	/**
	 * The ItemStacks that hold the items currently being used in the furnace
	 */
	private ItemStack[] furnaceItemStacks = new ItemStack[3];

	/** The number of ticks that the furnace will keep burning */
	public int furnaceBurnTime = 0;

	/**
	 * The number of ticks that a fresh copy of the currently-burning item would
	 * keep the furnace burning for
	 */
	public int currentItemBurnTime = 0;

	/** The number of ticks that the current item has been cooking for */
	public int furnaceCookTime = 0;
	private String field_94130_e;

	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory() {
		return this.furnaceItemStacks.length;
	}

	/**
	 * Returns the stack in slot i
	 */
	public ItemStack getStackInSlot(int par1) {
		return this.furnaceItemStacks[par1];
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number (second
	 * arg) of items and returns them in a new stack.
	 */
	public ItemStack decrStackSize(int par1, int par2) {
		if (this.furnaceItemStacks[par1] != null) {
			ItemStack var3;

			if (this.furnaceItemStacks[par1].stackSize <= par2) {
				var3 = this.furnaceItemStacks[par1];
				this.furnaceItemStacks[par1] = null;
				return var3;
			} else {
				var3 = this.furnaceItemStacks[par1].splitStack(par2);

				if (this.furnaceItemStacks[par1].stackSize == 0) {
					this.furnaceItemStacks[par1] = null;
				}

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
		if (this.furnaceItemStacks[par1] != null) {
			ItemStack var2 = this.furnaceItemStacks[par1];
			this.furnaceItemStacks[par1] = null;
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
		this.furnaceItemStacks[par1] = par2ItemStack;

		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	/**
	 * Returns the name of the inventory.
	 */
	public String getInvName() {
		return this.isInvNameLocalized() ? this.field_94130_e : "container.furnace";
	}

	/**
	 * If this returns false, the inventory name will be used as an unlocalized
	 * name, and translated into the player's language. Otherwise it will be used
	 * directly.
	 */
	public boolean isInvNameLocalized() {
		return this.field_94130_e != null && this.field_94130_e.length() > 0;
	}

	public void func_94129_a(String par1Str) {
		this.field_94130_e = par1Str;
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
			NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.furnaceItemStacks.length) {
				this.furnaceItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		this.furnaceBurnTime = par1NBTTagCompound.getShort("BurnTime");
		this.furnaceCookTime = par1NBTTagCompound.getShort("CookTime");
		this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);

		if (par1NBTTagCompound.hasKey("CustomName")) {
			this.field_94130_e = par1NBTTagCompound.getString("CustomName");
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setShort("BurnTime", (short) this.furnaceBurnTime);
		par1NBTTagCompound.setShort("CookTime", (short) this.furnaceCookTime);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.furnaceItemStacks.length; ++var3) {
			if (this.furnaceItemStacks[var3] != null) {
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.furnaceItemStacks[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		par1NBTTagCompound.setTag("Items", var2);

		if (this.isInvNameLocalized()) {
			par1NBTTagCompound.setString("CustomName", this.field_94130_e);
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
	 * Returns true if the furnace is currently burning
	 */
	public boolean isBurning() {
		return this.furnaceBurnTime > 0;
	}

	/**
	 * Allows the entity to update its state. Overridden in most subclasses, e.g.
	 * the mob spawner uses this to count ticks and creates a new spawn inside its
	 * implementation.
	 */
	public void updateEntity() {
		boolean var1 = this.furnaceBurnTime > 0;
		boolean var2 = false;

		if (this.furnaceBurnTime > 0) {
			--this.furnaceBurnTime;
		}

		if (!this.worldObj.isRemote) {
			if (this.furnaceBurnTime == 0 && this.canSmelt()) {
				this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);

				if (this.furnaceBurnTime > 0) {
					var2 = true;

					if (this.furnaceItemStacks[1] != null) {
						--this.furnaceItemStacks[1].stackSize;

						if (this.furnaceItemStacks[1].stackSize == 0) {
							Item var3 = this.furnaceItemStacks[1].getItem().getContainerItem();
							this.furnaceItemStacks[1] = var3 != null ? new ItemStack(var3) : null;
						}
					}
				}
			}

			if (this.isBurning() && this.canSmelt()) {
				++this.furnaceCookTime;

				if (this.furnaceCookTime == 200) {
					this.furnaceCookTime = 0;
					this.smeltItem();
					var2 = true;
				}
			} else {
				this.furnaceCookTime = 0;
			}

			if (var1 != this.furnaceBurnTime > 0) {
				var2 = true;
				BlockFurnace.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord,
						this.zCoord);
			}
		}

		if (var2) {
			this.onInventoryChanged();
		}
	}

	/**
	 * Returns true if the furnace can smelt an item, i.e. has a source item,
	 * destination stack isn't full, etc.
	 */
	private boolean canSmelt() {
		if (this.furnaceItemStacks[0] == null) {
			return false;
		} else {
			ItemStack var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0].getItem().itemID);
			return var1 == null ? false
					: (this.furnaceItemStacks[2] == null ? true
							: (!this.furnaceItemStacks[2].isItemEqual(var1) ? false
									: (this.furnaceItemStacks[2].stackSize < this.getInventoryStackLimit()
											&& this.furnaceItemStacks[2].stackSize < this.furnaceItemStacks[2]
													.getMaxStackSize() ? true
															: this.furnaceItemStacks[2].stackSize < var1
																	.getMaxStackSize())));
		}
	}

	/**
	 * Turn one item from the furnace source stack into the appropriate smelted item
	 * in the furnace result stack
	 */
	public void smeltItem() {
		if (this.canSmelt()) {
			ItemStack var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0].getItem().itemID);

			if (this.furnaceItemStacks[2] == null) {
				this.furnaceItemStacks[2] = var1.copy();
			} else if (this.furnaceItemStacks[2].itemID == var1.itemID) {
				++this.furnaceItemStacks[2].stackSize;
			}

			--this.furnaceItemStacks[0].stackSize;

			if (this.furnaceItemStacks[0].stackSize <= 0) {
				this.furnaceItemStacks[0] = null;
			}
		}
	}

	/**
	 * Returns the number of ticks that the supplied fuel item will keep the furnace
	 * burning, or 0 if the item isn't fuel
	 */
	public static int getItemBurnTime(ItemStack par0ItemStack) {
		if (par0ItemStack == null) {
			return 0;
		} else {
			int var1 = par0ItemStack.getItem().itemID;
			Item var2 = par0ItemStack.getItem();

			if (var1 < 256 && Block.blocksList[var1] != null) {
				Block var3 = Block.blocksList[var1];

				if (var3 == Block.woodSingleSlab) {
					return 150;
				}

				if (var3.blockMaterial == Material.wood) {
					return 300;
				}
			}

			return var2 instanceof ItemTool && ((ItemTool) var2).getToolMaterialName().equals("WOOD") ? 200
					: (var2 instanceof ItemSword && ((ItemSword) var2).getToolMaterialName().equals("WOOD") ? 200
							: (var2 instanceof ItemHoe && ((ItemHoe) var2).getMaterialName().equals("WOOD") ? 200
									: (var1 == Item.stick.itemID ? 100
											: (var1 == Item.coal.itemID ? 1600
													: (var1 == Item.bucketLava.itemID ? 20000
															: (var1 == Block.sapling.blockID ? 100
																	: (var1 == Item.blazeRod.itemID ? 2400 : 0)))))));
		}
	}

	/**
	 * Return true if item is a fuel source (getItemBurnTime() > 0).
	 */
	public static boolean isItemFuel(ItemStack par0ItemStack) {
		return getItemBurnTime(par0ItemStack) > 0;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes with
	 * Container
	 */
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false
				: par1EntityPlayer.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D,
						(double) this.zCoord + 0.5D) <= 64.0D;
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
		return par1 == 2 ? false : (par1 == 1 ? isItemFuel(par2ItemStack) : true);
	}

	/**
	 * param side
	 */
	public int[] getSlotsForFace(int par1) {
		return par1 == 0 ? field_102011_e : (par1 == 1 ? field_102010_d : field_102009_f);
	}

	/**
	 * Returns true if automation can insert the given item in the given slot from
	 * the given side. Args: Slot, item, side
	 */
	public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3) {
		return this.isStackValidForSlot(par1, par2ItemStack);
	}

	/**
	 * Returns true if automation can extract the given item in the given slot from
	 * the given side. Args: Slot, item, side
	 */
	public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3) {
		return par3 != 0 || par1 != 1 || par2ItemStack.itemID == Item.bucketEmpty.itemID;
	}
}
