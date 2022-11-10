package net.minecraft.src;

final class DispenserBehaviorFire extends BehaviorDefaultDispenseItem {
	private boolean field_96466_b = true;

	/**
	 * Dispense the specified stack, play the dispense sound and spawn particles.
	 */
	protected ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack) {
		EnumFacing var3 = BlockDispenser.getFacing(par1IBlockSource.getBlockMetadata());
		World var4 = par1IBlockSource.getWorld();
		int var5 = par1IBlockSource.getXInt() + var3.getFrontOffsetX();
		int var6 = par1IBlockSource.getYInt() + var3.getFrontOffsetY();
		int var7 = par1IBlockSource.getZInt() + var3.getFrontOffsetZ();

		if (var4.isAirBlock(var5, var6, var7)) {
			var4.setBlock(var5, var6, var7, Block.fire.blockID);

			if (par2ItemStack.func_96631_a(1, var4.rand)) {
				par2ItemStack.stackSize = 0;
			}
		} else if (var4.getBlockId(var5, var6, var7) == Block.tnt.blockID) {
			Block.tnt.onBlockDestroyedByPlayer(var4, var5, var6, var7, 1);
			var4.setBlockToAir(var5, var6, var7);
		} else {
			this.field_96466_b = false;
		}

		return par2ItemStack;
	}

	/**
	 * Play the dispense sound from the specified block.
	 */
	protected void playDispenseSound(IBlockSource par1IBlockSource) {
		if (this.field_96466_b) {
			par1IBlockSource.getWorld().playAuxSFX(1000, par1IBlockSource.getXInt(), par1IBlockSource.getYInt(),
					par1IBlockSource.getZInt(), 0);
		} else {
			par1IBlockSource.getWorld().playAuxSFX(1001, par1IBlockSource.getXInt(), par1IBlockSource.getYInt(),
					par1IBlockSource.getZInt(), 0);
		}
	}
}
