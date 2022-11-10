package net.minecraft.src;

import java.util.List;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockStep extends BlockHalfSlab {
	/** The list of the types of step blocks. */
	public static final String[] blockStepTypes = new String[] { "stone", "sand", "wood", "cobble", "brick", "smoothStoneBrick", "netherBrick", "quartz" };
	private Icon theIcon;

	public BlockStep(int par1, boolean par2) {
		super(par1, par2, Material.rock);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		int var3 = par2 & 7;

		if (this.isDoubleSlab && (par2 & 8) != 0) {
			par1 = 1;
		}

		return var3 == 0 ? (par1 != 1 && par1 != 0 ? this.theIcon : this.blockIcon)
				: (var3 == 1 ? Block.sandStone.getBlockTextureFromSide(par1)
						: (var3 == 2 ? Block.planks.getBlockTextureFromSide(par1)
								: (var3 == 3 ? Block.cobblestone.getBlockTextureFromSide(par1)
										: (var3 == 4 ? Block.brick.getBlockTextureFromSide(par1)
												: (var3 == 5 ? Block.stoneBrick.getIcon(par1, 0)
														: (var3 == 6 ? Block.netherBrick.getBlockTextureFromSide(1) : (var3 == 7 ? Block.blockNetherQuartz.getBlockTextureFromSide(par1) : this.blockIcon)))))));
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("stoneslab_top");
		this.theIcon = par1IconRegister.registerIcon("stoneslab_side");
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Block.stoneSingleSlab.blockID;
	}

	/**
	 * Returns an item stack containing a single instance of the current block type.
	 * 'i' is the block's subtype/damage and is ignored for blocks which do not
	 * support subtypes. Blocks which cannot be harvested should return null.
	 */
	protected ItemStack createStackedBlock(int par1) {
		return new ItemStack(Block.stoneSingleSlab.blockID, 2, par1 & 7);
	}

	/**
	 * Returns the slab block name with step type.
	 */
	public String getFullSlabName(int par1) {
		if (par1 < 0 || par1 >= blockStepTypes.length) {
			par1 = 0;
		}

		return super.getUnlocalizedName() + "." + blockStepTypes[par1];
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		if (par1 != Block.stoneDoubleSlab.blockID) {
			for (int var4 = 0; var4 <= 7; ++var4) {
				if (var4 != 2) {
					par3List.add(new ItemStack(par1, 1, var4));
				}
			}
		}
	}
}
