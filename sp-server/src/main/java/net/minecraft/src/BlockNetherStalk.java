package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockNetherStalk extends BlockFlower {
	private static final String[] field_94373_a = new String[] { "netherStalk_0", "netherStalk_1", "netherStalk_2" };

	protected BlockNetherStalk(int par1) {
		super(par1);
		this.setTickRandomly(true);
		float var2 = 0.5F;
		this.setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, 0.25F, 0.5F + var2);
		this.setCreativeTab((CreativeTabs) null);
	}

	/**
	 * Gets passed in the blockID of the block below and supposed to return true if
	 * its allowed to grow on the type of blockID passed in. Args: blockID
	 */
	protected boolean canThisPlantGrowOnThisBlockID(int par1) {
		return par1 == Block.slowSand.blockID;
	}

	/**
	 * Can this block stay at this position. Similar to canPlaceBlockAt except gets
	 * checked often with plants.
	 */
	public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
		return this.canThisPlantGrowOnThisBlockID(par1World.getBlockId(par2, par3 - 1, par4));
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		int var6 = par1World.getBlockMetadata(par2, par3, par4);

		if (var6 < 3 && par5Random.nextInt(10) == 0) {
			++var6;
			par1World.setBlockMetadata(par2, par3, par4, var6, 2);
		}

		super.updateTick(par1World, par2, par3, par4, par5Random);
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 6;
	}

	/**
	 * Drops the block items with a specified chance of dropping the specified items
	 */
	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6,
			int par7) {
		if (!par1World.isRemote) {
			int var8 = 1;

			if (par5 >= 3) {
				var8 = 2 + par1World.rand.nextInt(3);

				if (par7 > 0) {
					var8 += par1World.rand.nextInt(par7 + 1);
				}
			}

			for (int var9 = 0; var9 < var8; ++var9) {
				this.dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(Item.netherStalkSeeds));
			}
		}
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return 0;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom par1Random) {
		return 0;
	}
}
