package net.minecraft.src;

public class BlockPotato extends BlockCrops {
	public BlockPotato(int par1) {
		super(par1);
	}

	/**
	 * Generate a seed ItemStack for this crop.
	 */
	protected int getSeedItem() {
		return Item.potato.itemID;
	}

	/**
	 * Generate a crop produce ItemStack for this crop.
	 */
	protected int getCropItem() {
		return Item.potato.itemID;
	}

	/**
	 * Drops the block items with a specified chance of dropping the specified items
	 */
	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6,
			int par7) {
		super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, par7);

		if (!par1World.isRemote) {
			if (par5 >= 7 && par1World.rand.nextInt(50) == 0) {
				this.dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(Item.poisonousPotato));
			}
		}
	}
}
