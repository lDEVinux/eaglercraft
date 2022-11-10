package net.minecraft.src;

public class ContainerBeacon extends Container {
	private TileEntityBeacon theBeacon;

	/**
	 * This beacon's slot where you put in Emerald, Diamond, Gold or Iron Ingot.
	 */
	private final SlotBeacon beaconSlot;
	private int field_82865_g;
	private int field_82867_h;
	private int field_82868_i;

	public ContainerBeacon(InventoryPlayer par1InventoryPlayer, TileEntityBeacon par2TileEntityBeacon) {
		this.theBeacon = par2TileEntityBeacon;
		this.addSlotToContainer(this.beaconSlot = new SlotBeacon(this, par2TileEntityBeacon, 0, 136, 110));
		byte var3 = 36;
		short var4 = 137;
		int var5;

		for (var5 = 0; var5 < 3; ++var5) {
			for (int var6 = 0; var6 < 9; ++var6) {
				this.addSlotToContainer(
						new Slot(par1InventoryPlayer, var6 + var5 * 9 + 9, var3 + var6 * 18, var4 + var5 * 18));
			}
		}

		for (var5 = 0; var5 < 9; ++var5) {
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var5, var3 + var5 * 18, 58 + var4));
		}

		this.field_82865_g = par2TileEntityBeacon.getLevels();
		this.field_82867_h = par2TileEntityBeacon.getPrimaryEffect();
		this.field_82868_i = par2TileEntityBeacon.getSecondaryEffect();
	}

	public void onCraftGuiOpened(ICrafting par1ICrafting) {
		super.onCraftGuiOpened(par1ICrafting);
		par1ICrafting.sendProgressBarUpdate(this, 0, this.field_82865_g);
		par1ICrafting.sendProgressBarUpdate(this, 1, this.field_82867_h);
		par1ICrafting.sendProgressBarUpdate(this, 2, this.field_82868_i);
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
	}

	/**
	 * Returns the Tile Entity behind this beacon inventory / container
	 */
	public TileEntityBeacon getBeacon() {
		return this.theBeacon;
	}

	public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
		return this.theBeacon.isUseableByPlayer(par1EntityPlayer);
	}

	/**
	 * Take a stack from the specified inventory slot.
	 */
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		ItemStack var3 = null;
		Slot var4 = (Slot) this.inventorySlots.get(par2);

		if (var4 != null && var4.getHasStack()) {
			ItemStack var5 = var4.getStack();
			var3 = var5.copy();

			if (par2 == 0) {
				if (!this.mergeItemStack(var5, 1, 37, true)) {
					return null;
				}

				var4.onSlotChange(var5, var3);
			} else if (!this.beaconSlot.getHasStack() && this.beaconSlot.isItemValid(var5) && var5.stackSize == 1) {
				if (!this.mergeItemStack(var5, 0, 1, false)) {
					return null;
				}
			} else if (par2 >= 1 && par2 < 28) {
				if (!this.mergeItemStack(var5, 28, 37, false)) {
					return null;
				}
			} else if (par2 >= 28 && par2 < 37) {
				if (!this.mergeItemStack(var5, 1, 28, false)) {
					return null;
				}
			} else if (!this.mergeItemStack(var5, 1, 37, false)) {
				return null;
			}

			if (var5.stackSize == 0) {
				var4.putStack((ItemStack) null);
			} else {
				var4.onSlotChanged();
			}

			if (var5.stackSize == var3.stackSize) {
				return null;
			}

			var4.onPickupFromSlot(par1EntityPlayer, var5);
		}

		return var3;
	}
}
