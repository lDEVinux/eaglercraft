package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockWoodSlab extends BlockHalfSlab {
	/** The type of tree this slab came from. */
	public static final String[] woodType = new String[] { "oak", "spruce", "birch", "jungle" };

	public BlockWoodSlab(int par1, boolean par2) {
		super(par1, par2, Material.wood);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Block.woodSingleSlab.blockID;
	}

	/**
	 * Returns an item stack containing a single instance of the current block type.
	 * 'i' is the block's subtype/damage and is ignored for blocks which do not
	 * support subtypes. Blocks which cannot be harvested should return null.
	 */
	protected ItemStack createStackedBlock(int par1) {
		return new ItemStack(Block.woodSingleSlab.blockID, 2, par1 & 7);
	}

	/**
	 * Returns the slab block name with step type.
	 */
	public String getFullSlabName(int par1) {
		if (par1 < 0 || par1 >= woodType.length) {
			par1 = 0;
		}

		return super.getUnlocalizedName() + "." + woodType[par1];
	}
}
