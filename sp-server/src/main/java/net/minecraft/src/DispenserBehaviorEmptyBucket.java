package net.minecraft.src;

final class DispenserBehaviorEmptyBucket extends BehaviorDefaultDispenseItem {
	private final BehaviorDefaultDispenseItem defaultDispenserItemBehavior = new BehaviorDefaultDispenseItem();

	/**
	 * Dispense the specified stack, play the dispense sound and spawn particles.
	 */
	public ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack) {
		EnumFacing var3 = BlockDispenser.getFacing(par1IBlockSource.getBlockMetadata());
		World var4 = par1IBlockSource.getWorld();
		int var5 = par1IBlockSource.getXInt() + var3.getFrontOffsetX();
		int var6 = par1IBlockSource.getYInt() + var3.getFrontOffsetY();
		int var7 = par1IBlockSource.getZInt() + var3.getFrontOffsetZ();
		Material var8 = var4.getBlockMaterial(var5, var6, var7);
		int var9 = var4.getBlockMetadata(var5, var6, var7);
		Item var10;

		if (Material.water.equals(var8) && var9 == 0) {
			var10 = Item.bucketWater;
		} else {
			if (!Material.lava.equals(var8) || var9 != 0) {
				return super.dispenseStack(par1IBlockSource, par2ItemStack);
			}

			var10 = Item.bucketLava;
		}

		var4.setBlockToAir(var5, var6, var7);

		if (--par2ItemStack.stackSize == 0) {
			par2ItemStack.itemID = var10.itemID;
			par2ItemStack.stackSize = 1;
		} else if (((TileEntityDispenser) par1IBlockSource.getBlockTileEntity()).addItem(new ItemStack(var10)) < 0) {
			this.defaultDispenserItemBehavior.dispense(par1IBlockSource, new ItemStack(var10));
		}

		return par2ItemStack;
	}
}
