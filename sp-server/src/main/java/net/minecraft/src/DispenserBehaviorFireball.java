package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

final class DispenserBehaviorFireball extends BehaviorDefaultDispenseItem {
	/**
	 * Dispense the specified stack, play the dispense sound and spawn particles.
	 */
	public ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack) {
		EnumFacing var3 = BlockDispenser.getFacing(par1IBlockSource.getBlockMetadata());
		IPosition var4 = BlockDispenser.getIPositionFromBlockSource(par1IBlockSource);
		double var5 = var4.getX() + (double) ((float) var3.getFrontOffsetX() * 0.3F);
		double var7 = var4.getY() + (double) ((float) var3.getFrontOffsetX() * 0.3F);
		double var9 = var4.getZ() + (double) ((float) var3.getFrontOffsetZ() * 0.3F);
		World var11 = par1IBlockSource.getWorld();
		EaglercraftRandom var12 = var11.rand;
		double var13 = var12.nextGaussian() * 0.05D + (double) var3.getFrontOffsetX();
		double var15 = var12.nextGaussian() * 0.05D + (double) var3.getFrontOffsetY();
		double var17 = var12.nextGaussian() * 0.05D + (double) var3.getFrontOffsetZ();
		var11.spawnEntityInWorld(new EntitySmallFireball(var11, var5, var7, var9, var13, var15, var17));
		par2ItemStack.splitStack(1);
		return par2ItemStack;
	}

	/**
	 * Play the dispense sound from the specified block.
	 */
	protected void playDispenseSound(IBlockSource par1IBlockSource) {
		par1IBlockSource.getWorld().playAuxSFX(1009, par1IBlockSource.getXInt(), par1IBlockSource.getYInt(),
				par1IBlockSource.getZInt(), 0);
	}
}
