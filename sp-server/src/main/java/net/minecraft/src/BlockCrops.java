package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockCrops extends BlockFlower {
	protected BlockCrops(int par1) {
		super(par1);
		this.setTickRandomly(true);
		float var2 = 0.5F;
		this.setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, 0.25F, 0.5F + var2);
		this.setCreativeTab((CreativeTabs) null);
		this.setHardness(0.0F);
		this.setStepSound(soundGrassFootstep);
		this.disableStats();
	}

	/**
	 * Gets passed in the blockID of the block below and supposed to return true if
	 * its allowed to grow on the type of blockID passed in. Args: blockID
	 */
	protected boolean canThisPlantGrowOnThisBlockID(int par1) {
		return par1 == Block.tilledField.blockID;
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		super.updateTick(par1World, par2, par3, par4, par5Random);

		if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9) {
			int var6 = par1World.getBlockMetadata(par2, par3, par4);

			if (var6 < 7) {
				float var7 = this.getGrowthRate(par1World, par2, par3, par4);

				if (par5Random.nextInt((int) (25.0F / var7) + 1) == 0) {
					++var6;
					par1World.setBlockMetadata(par2, par3, par4, var6, 2);
				}
			}
		}
	}

	/**
	 * Apply bonemeal to the crops.
	 */
	public void fertilize(World par1World, int par2, int par3, int par4) {
		int var5 = par1World.getBlockMetadata(par2, par3, par4)
				+ MathHelper.getRandomIntegerInRange(par1World.rand, 2, 5);

		if (var5 > 7) {
			var5 = 7;
		}

		par1World.setBlockMetadata(par2, par3, par4, var5, 2);
	}

	/**
	 * Gets the growth rate for the crop. Setup to encourage rows by halving growth
	 * rate if there is diagonals, crops on different sides that aren't opposing,
	 * and by adding growth for every crop next to this one (and for crop below this
	 * one). Args: x, y, z
	 */
	private float getGrowthRate(World par1World, int par2, int par3, int par4) {
		float var5 = 1.0F;
		int var6 = par1World.getBlockId(par2, par3, par4 - 1);
		int var7 = par1World.getBlockId(par2, par3, par4 + 1);
		int var8 = par1World.getBlockId(par2 - 1, par3, par4);
		int var9 = par1World.getBlockId(par2 + 1, par3, par4);
		int var10 = par1World.getBlockId(par2 - 1, par3, par4 - 1);
		int var11 = par1World.getBlockId(par2 + 1, par3, par4 - 1);
		int var12 = par1World.getBlockId(par2 + 1, par3, par4 + 1);
		int var13 = par1World.getBlockId(par2 - 1, par3, par4 + 1);
		boolean var14 = var8 == this.blockID || var9 == this.blockID;
		boolean var15 = var6 == this.blockID || var7 == this.blockID;
		boolean var16 = var10 == this.blockID || var11 == this.blockID || var12 == this.blockID
				|| var13 == this.blockID;

		for (int var17 = par2 - 1; var17 <= par2 + 1; ++var17) {
			for (int var18 = par4 - 1; var18 <= par4 + 1; ++var18) {
				int var19 = par1World.getBlockId(var17, par3 - 1, var18);
				float var20 = 0.0F;

				if (var19 == Block.tilledField.blockID) {
					var20 = 1.0F;

					if (par1World.getBlockMetadata(var17, par3 - 1, var18) > 0) {
						var20 = 3.0F;
					}
				}

				if (var17 != par2 || var18 != par4) {
					var20 /= 4.0F;
				}

				var5 += var20;
			}
		}

		if (var16 || var14 && var15) {
			var5 /= 2.0F;
		}

		return var5;
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 6;
	}

	/**
	 * Generate a seed ItemStack for this crop.
	 */
	protected int getSeedItem() {
		return Item.seeds.itemID;
	}

	/**
	 * Generate a crop produce ItemStack for this crop.
	 */
	protected int getCropItem() {
		return Item.wheat.itemID;
	}

	/**
	 * Drops the block items with a specified chance of dropping the specified items
	 */
	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6,
			int par7) {
		super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, 0);

		if (!par1World.isRemote) {
			if (par5 >= 7) {
				int var8 = 3 + par7;

				for (int var9 = 0; var9 < var8; ++var9) {
					if (par1World.rand.nextInt(15) <= par5) {
						this.dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(this.getSeedItem(), 1, 0));
					}
				}
			}
		}
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return par1 == 7 ? this.getCropItem() : this.getSeedItem();
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom par1Random) {
		return 1;
	}
}
