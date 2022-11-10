package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockSapling extends BlockFlower {
	public static final String[] WOOD_TYPES = new String[] { "oak", "spruce", "birch", "jungle" };
	private static final String[] field_94370_b = new String[] { "sapling", "sapling_spruce", "sapling_birch",
			"sapling_jungle" };

	protected BlockSapling(int par1) {
		super(par1);
		float var2 = 0.4F;
		this.setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, var2 * 2.0F, 0.5F + var2);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		if (!par1World.isRemote) {
			super.updateTick(par1World, par2, par3, par4, par5Random);

			if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9 && par5Random.nextInt(7) == 0) {
				this.markOrGrowMarked(par1World, par2, par3, par4, par5Random);
			}
		}
	}

	public void markOrGrowMarked(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		int var6 = par1World.getBlockMetadata(par2, par3, par4);

		if ((var6 & 8) == 0) {
			par1World.setBlockMetadata(par2, par3, par4, var6 | 8, 4);
		} else {
			this.growTree(par1World, par2, par3, par4, par5Random);
		}
	}

	/**
	 * Attempts to grow a sapling into a tree
	 */
	public void growTree(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		int var6 = par1World.getBlockMetadata(par2, par3, par4) & 3;
		Object var7 = null;
		int var8 = 0;
		int var9 = 0;
		boolean var10 = false;

		if (var6 == 1) {
			var7 = new WorldGenTaiga2(true);
		} else if (var6 == 2) {
			var7 = new WorldGenForest(true);
		} else if (var6 == 3) {
			for (var8 = 0; var8 >= -1; --var8) {
				for (var9 = 0; var9 >= -1; --var9) {
					if (this.isSameSapling(par1World, par2 + var8, par3, par4 + var9, 3)
							&& this.isSameSapling(par1World, par2 + var8 + 1, par3, par4 + var9, 3)
							&& this.isSameSapling(par1World, par2 + var8, par3, par4 + var9 + 1, 3)
							&& this.isSameSapling(par1World, par2 + var8 + 1, par3, par4 + var9 + 1, 3)) {
						var7 = new WorldGenHugeTrees(true, 10 + par5Random.nextInt(20), 3, 3);
						var10 = true;
						break;
					}
				}

				if (var7 != null) {
					break;
				}
			}

			if (var7 == null) {
				var9 = 0;
				var8 = 0;
				var7 = new WorldGenTrees(true, 4 + par5Random.nextInt(7), 3, 3, false);
			}
		} else {
			var7 = new WorldGenTrees(true);

			if (par5Random.nextInt(10) == 0) {
				var7 = new WorldGenBigTree(true);
			}
		}

		if (var10) {
			par1World.setBlock(par2 + var8, par3, par4 + var9, 0, 0, 4);
			par1World.setBlock(par2 + var8 + 1, par3, par4 + var9, 0, 0, 4);
			par1World.setBlock(par2 + var8, par3, par4 + var9 + 1, 0, 0, 4);
			par1World.setBlock(par2 + var8 + 1, par3, par4 + var9 + 1, 0, 0, 4);
		} else {
			par1World.setBlock(par2, par3, par4, 0, 0, 4);
		}

		if (!((WorldGenerator) var7).generate(par1World, par5Random, par2 + var8, par3, par4 + var9)) {
			if (var10) {
				par1World.setBlock(par2 + var8, par3, par4 + var9, this.blockID, var6, 4);
				par1World.setBlock(par2 + var8 + 1, par3, par4 + var9, this.blockID, var6, 4);
				par1World.setBlock(par2 + var8, par3, par4 + var9 + 1, this.blockID, var6, 4);
				par1World.setBlock(par2 + var8 + 1, par3, par4 + var9 + 1, this.blockID, var6, 4);
			} else {
				par1World.setBlock(par2, par3, par4, this.blockID, var6, 4);
			}
		}
	}

	/**
	 * Determines if the same sapling is present at the given location.
	 */
	public boolean isSameSapling(World par1World, int par2, int par3, int par4, int par5) {
		return par1World.getBlockId(par2, par3, par4) == this.blockID
				&& (par1World.getBlockMetadata(par2, par3, par4) & 3) == par5;
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and wood.
	 */
	public int damageDropped(int par1) {
		return par1 & 3;
	}
}
