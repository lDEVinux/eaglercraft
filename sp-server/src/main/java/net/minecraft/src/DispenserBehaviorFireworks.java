package net.minecraft.src;

final class DispenserBehaviorFireworks extends BehaviorDefaultDispenseItem {
	/**
	 * Dispense the specified stack, play the dispense sound and spawn particles.
	 */
	public ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack) {
		EnumFacing var3 = BlockDispenser.getFacing(par1IBlockSource.getBlockMetadata());
		double var4 = par1IBlockSource.getX() + (double) var3.getFrontOffsetX();
		double var6 = (double) ((float) par1IBlockSource.getYInt() + 0.2F);
		double var8 = par1IBlockSource.getZ() + (double) var3.getFrontOffsetZ();
		EntityFireworkRocket var10 = new EntityFireworkRocket(par1IBlockSource.getWorld(), var4, var6, var8,
				par2ItemStack);
		par1IBlockSource.getWorld().spawnEntityInWorld(var10);
		par2ItemStack.splitStack(1);
		return par2ItemStack;
	}

	/**
	 * Play the dispense sound from the specified block.
	 */
	protected void playDispenseSound(IBlockSource par1IBlockSource) {
		par1IBlockSource.getWorld().playAuxSFX(1002, par1IBlockSource.getXInt(), par1IBlockSource.getYInt(),
				par1IBlockSource.getZInt(), 0);
	}
}
