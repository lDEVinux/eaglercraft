package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockTallGrass extends BlockFlower {
	private static final String[] grassTypes = new String[] { "deadbush", "tallgrass", "fern" };

	protected BlockTallGrass(int par1) {
		super(par1, Material.vine);
		float var2 = 0.4F;
		this.setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, 0.8F, 0.5F + var2);
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return par2Random.nextInt(8) == 0 ? Item.seeds.itemID : -1;
	}

	/**
	 * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i'
	 * (inclusive).
	 */
	public int quantityDroppedWithBonus(int par1, EaglercraftRandom par2Random) {
		return 1 + par2Random.nextInt(par1 * 2 + 1);
	}

	/**
	 * Called when the player destroys a block with an item that can harvest it. (i,
	 * j, k) are the coordinates of the block and l is the block's subtype/damage.
	 */
	public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
		if (!par1World.isRemote && par2EntityPlayer.getCurrentEquippedItem() != null
				&& par2EntityPlayer.getCurrentEquippedItem().itemID == Item.shears.itemID) {
			par2EntityPlayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
			this.dropBlockAsItem_do(par1World, par3, par4, par5, new ItemStack(Block.tallGrass, 1, par6));
		} else {
			super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
		}
	}

	/**
	 * Get the block's damage value (for use with pick block).
	 */
	public int getDamageValue(World par1World, int par2, int par3, int par4) {
		return par1World.getBlockMetadata(par2, par3, par4);
	}
}
