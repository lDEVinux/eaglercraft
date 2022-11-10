package net.minecraft.src;

class SlotRepair extends Slot {
	final World theWorld;

	final int blockPosX;

	final int blockPosY;

	final int blockPosZ;

	/** The anvil this slot belongs to. */
	final ContainerRepair anvil;

	SlotRepair(ContainerRepair par1ContainerRepair, IInventory par2IInventory, int par3, int par4, int par5,
			World par6World, int par7, int par8, int par9) {
		super(par2IInventory, par3, par4, par5);
		this.anvil = par1ContainerRepair;
		this.theWorld = par6World;
		this.blockPosX = par7;
		this.blockPosY = par8;
		this.blockPosZ = par9;
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for the
	 * armor slots.
	 */
	public boolean isItemValid(ItemStack par1ItemStack) {
		return false;
	}

	/**
	 * Return whether this slot's stack can be taken from this slot.
	 */
	public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
		return (par1EntityPlayer.capabilities.isCreativeMode
				|| par1EntityPlayer.experienceLevel >= this.anvil.maximumCost) && this.anvil.maximumCost > 0
				&& this.getHasStack();
	}

	public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack) {
		if (!par1EntityPlayer.capabilities.isCreativeMode) {
			par1EntityPlayer.addExperienceLevel(-this.anvil.maximumCost);
		}

		ContainerRepair.getRepairInputInventory(this.anvil).setInventorySlotContents(0, (ItemStack) null);

		if (ContainerRepair.getStackSizeUsedInRepair(this.anvil) > 0) {
			ItemStack var3 = ContainerRepair.getRepairInputInventory(this.anvil).getStackInSlot(1);

			if (var3 != null && var3.stackSize > ContainerRepair.getStackSizeUsedInRepair(this.anvil)) {
				var3.stackSize -= ContainerRepair.getStackSizeUsedInRepair(this.anvil);
				ContainerRepair.getRepairInputInventory(this.anvil).setInventorySlotContents(1, var3);
			} else {
				ContainerRepair.getRepairInputInventory(this.anvil).setInventorySlotContents(1, (ItemStack) null);
			}
		} else {
			ContainerRepair.getRepairInputInventory(this.anvil).setInventorySlotContents(1, (ItemStack) null);
		}

		this.anvil.maximumCost = 0;

		if (!par1EntityPlayer.capabilities.isCreativeMode && !this.theWorld.isRemote
				&& this.theWorld.getBlockId(this.blockPosX, this.blockPosY, this.blockPosZ) == Block.anvil.blockID
				&& par1EntityPlayer.getRNG().nextFloat() < 0.12F) {
			int var6 = this.theWorld.getBlockMetadata(this.blockPosX, this.blockPosY, this.blockPosZ);
			int var4 = var6 & 3;
			int var5 = var6 >> 2;
			++var5;

			if (var5 > 2) {
				this.theWorld.setBlockToAir(this.blockPosX, this.blockPosY, this.blockPosZ);
				this.theWorld.playAuxSFX(1020, this.blockPosX, this.blockPosY, this.blockPosZ, 0);
			} else {
				this.theWorld.setBlockMetadata(this.blockPosX, this.blockPosY, this.blockPosZ, var4 | var5 << 2, 2);
				this.theWorld.playAuxSFX(1021, this.blockPosX, this.blockPosY, this.blockPosZ, 0);
			}
		} else if (!this.theWorld.isRemote) {
			this.theWorld.playAuxSFX(1021, this.blockPosX, this.blockPosY, this.blockPosZ, 0);
		}
	}
}
