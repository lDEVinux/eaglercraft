package net.minecraft.src;

final class DispenserBehaviorMobEgg extends BehaviorDefaultDispenseItem {
	/**
	 * Dispense the specified stack, play the dispense sound and spawn particles.
	 */
	public ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack) {
		EnumFacing var3 = BlockDispenser.getFacing(par1IBlockSource.getBlockMetadata());
		double var4 = par1IBlockSource.getX() + (double) var3.getFrontOffsetX();
		double var6 = (double) ((float) par1IBlockSource.getYInt() + 0.2F);
		double var8 = par1IBlockSource.getZ() + (double) var3.getFrontOffsetZ();
		Entity var10 = ItemMonsterPlacer.spawnCreature(par1IBlockSource.getWorld(), par2ItemStack.getItemDamage(), var4,
				var6, var8);

		if (var10 instanceof EntityLiving && par2ItemStack.hasDisplayName()) {
			((EntityLiving) var10).func_94058_c(par2ItemStack.getDisplayName());
		}

		par2ItemStack.splitStack(1);
		return par2ItemStack;
	}
}
