package net.minecraft.src;



public class SlotFurnace extends Slot {
	/** The player that is using the GUI where this slot resides. */
	private EntityPlayer thePlayer;
	private int field_75228_b;

	public SlotFurnace(EntityPlayer par1EntityPlayer, IInventory par2IInventory, int par3, int par4, int par5) {
		super(par2IInventory, par3, par4, par5);
		this.thePlayer = par1EntityPlayer;
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for the
	 * armor slots.
	 */
	public boolean isItemValid(ItemStack par1ItemStack) {
		return false;
	}

	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of the
	 * second int arg. Returns the new stack.
	 */
	public ItemStack decrStackSize(int par1) {
		if (this.getHasStack()) {
			this.field_75228_b += Math.min(par1, this.getStack().stackSize);
		}

		return super.decrStackSize(par1);
	}

	public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack) {
		this.onCrafting(par2ItemStack);
		super.onPickupFromSlot(par1EntityPlayer, par2ItemStack);
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not
	 * ore and wood. Typically increases an internal count then calls
	 * onCrafting(item).
	 */
	protected void onCrafting(ItemStack par1ItemStack, int par2) {
		this.field_75228_b += par2;
		this.onCrafting(par1ItemStack);
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not
	 * ore and wood.
	 */
	protected void onCrafting(ItemStack par1ItemStack) {
		par1ItemStack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75228_b);

		this.field_75228_b = 0;

		if (par1ItemStack.itemID == Item.ingotIron.itemID) {
			this.thePlayer.addStat(AchievementList.acquireIron, 1);
		}

		if (par1ItemStack.itemID == Item.fishCooked.itemID) {
			this.thePlayer.addStat(AchievementList.cookFish, 1);
		}
	}
}
