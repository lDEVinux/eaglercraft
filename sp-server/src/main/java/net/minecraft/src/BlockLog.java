package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockLog extends Block {
	/** The type of tree this log came from. */
	public static final String[] woodType = new String[] { "oak", "spruce", "birch", "jungle" };
	public static final String[] treeTextureTypes = new String[] { "tree_side", "tree_spruce", "tree_birch",
			"tree_jungle" };

	protected BlockLog(int par1) {
		super(par1, Material.wood);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 31;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom par1Random) {
		return 1;
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Block.wood.blockID;
	}

	/**
	 * ejects contained items into the world, and notifies neighbours of an update,
	 * as appropriate
	 */
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		byte var7 = 4;
		int var8 = var7 + 1;

		if (par1World.checkChunksExist(par2 - var8, par3 - var8, par4 - var8, par2 + var8, par3 + var8, par4 + var8)) {
			for (int var9 = -var7; var9 <= var7; ++var9) {
				for (int var10 = -var7; var10 <= var7; ++var10) {
					for (int var11 = -var7; var11 <= var7; ++var11) {
						int var12 = par1World.getBlockId(par2 + var9, par3 + var10, par4 + var11);

						if (var12 == Block.leaves.blockID) {
							int var13 = par1World.getBlockMetadata(par2 + var9, par3 + var10, par4 + var11);

							if ((var13 & 8) == 0) {
								par1World.setBlockMetadata(par2 + var9, par3 + var10, par4 + var11, var13 | 8, 4);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z,
	 * side, hitX, hitY, hitZ, block metadata
	 */
	public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7,
			float par8, int par9) {
		int var10 = par9 & 3;
		byte var11 = 0;

		switch (par5) {
		case 0:
		case 1:
			var11 = 0;
			break;

		case 2:
		case 3:
			var11 = 8;
			break;

		case 4:
		case 5:
			var11 = 4;
		}

		return var10 | var11;
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and wood.
	 */
	public int damageDropped(int par1) {
		return par1 & 3;
	}

	/**
	 * returns a number between 0 and 3
	 */
	public static int limitToValidMetadata(int par0) {
		return par0 & 3;
	}

	/**
	 * Returns an item stack containing a single instance of the current block type.
	 * 'i' is the block's subtype/damage and is ignored for blocks which do not
	 * support subtypes. Blocks which cannot be harvested should return null.
	 */
	protected ItemStack createStackedBlock(int par1) {
		return new ItemStack(this.blockID, 1, limitToValidMetadata(par1));
	}
}
