package net.minecraft.src;

final class DispenserBehaviorFilledBucket extends BehaviorDefaultDispenseItem {
	private final BehaviorDefaultDispenseItem defaultDispenserItemBehavior = new BehaviorDefaultDispenseItem();

	/**
	 * Dispense the specified stack, play the dispense sound and spawn particles.
	 */
	public ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack) {
		ItemBucket var3 = (ItemBucket) par2ItemStack.getItem();
		int var4 = par1IBlockSource.getXInt();
		int var5 = par1IBlockSource.getYInt();
		int var6 = par1IBlockSource.getZInt();
		EnumFacing var7 = BlockDispenser.getFacing(par1IBlockSource.getBlockMetadata());

		if (var3.tryPlaceContainedLiquid(par1IBlockSource.getWorld(), (double) var4, (double) var5, (double) var6,
				var4 + var7.getFrontOffsetX(), var5 + var7.getFrontOffsetY(), var6 + var7.getFrontOffsetZ())) {
			par2ItemStack.itemID = Item.bucketEmpty.itemID;
			par2ItemStack.stackSize = 1;
			return par2ItemStack;
		} else {
			return this.defaultDispenserItemBehavior.dispense(par1IBlockSource, par2ItemStack);
		}
	}
}
