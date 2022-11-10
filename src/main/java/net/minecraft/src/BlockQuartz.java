package net.minecraft.src;

import java.util.List;

public class BlockQuartz extends Block {
	public static final String[] quartzBlockTypes = new String[] { "default", "chiseled", "lines" };
	private static final String[] quartzBlockTextureTypes = new String[] { "quartzblock_side", "quartzblock_chiseled", "quartzblock_lines", null, null };
	private Icon[] quartzblockIcons;
	private Icon quartzblock_chiseled_top;
	private Icon quartzblock_lines_top;
	private Icon quartzblock_top;
	private Icon quartzblock_bottom;

	public BlockQuartz(int par1) {
		super(par1, Material.rock);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		if (par2 != 2 && par2 != 3 && par2 != 4) {
			if (par1 != 1 && (par1 != 0 || par2 != 1)) {
				if (par1 == 0) {
					return this.quartzblock_bottom;
				} else {
					if (par2 < 0 || par2 >= this.quartzblockIcons.length) {
						par2 = 0;
					}

					return this.quartzblockIcons[par2];
				}
			} else {
				return par2 == 1 ? this.quartzblock_chiseled_top : this.quartzblock_top;
			}
		} else {
			return par2 == 2 && (par1 == 1 || par1 == 0) ? this.quartzblock_lines_top
					: (par2 == 3 && (par1 == 5 || par1 == 4) ? this.quartzblock_lines_top : (par2 == 4 && (par1 == 2 || par1 == 3) ? this.quartzblock_lines_top : this.quartzblockIcons[par2]));
		}
	}

	/**
	 * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z,
	 * side, hitX, hitY, hitZ, block metadata
	 */
	public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9) {
		if (par9 == 2) {
			switch (par5) {
			case 0:
			case 1:
				par9 = 2;
				break;

			case 2:
			case 3:
				par9 = 4;
				break;

			case 4:
			case 5:
				par9 = 3;
			}
		}

		return par9;
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and wood.
	 */
	public int damageDropped(int par1) {
		return par1 != 3 && par1 != 4 ? par1 : 2;
	}

	/**
	 * Returns an item stack containing a single instance of the current block type.
	 * 'i' is the block's subtype/damage and is ignored for blocks which do not
	 * support subtypes. Blocks which cannot be harvested should return null.
	 */
	protected ItemStack createStackedBlock(int par1) {
		return par1 != 3 && par1 != 4 ? super.createStackedBlock(par1) : new ItemStack(this.blockID, 1, 2);
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 39;
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
		par3List.add(new ItemStack(par1, 1, 2));
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.quartzblockIcons = new Icon[quartzBlockTextureTypes.length];

		for (int var2 = 0; var2 < this.quartzblockIcons.length; ++var2) {
			if (quartzBlockTextureTypes[var2] == null) {
				this.quartzblockIcons[var2] = this.quartzblockIcons[var2 - 1];
			} else {
				this.quartzblockIcons[var2] = par1IconRegister.registerIcon(quartzBlockTextureTypes[var2]);
			}
		}

		this.quartzblock_top = par1IconRegister.registerIcon("quartzblock_top");
		this.quartzblock_chiseled_top = par1IconRegister.registerIcon("quartzblock_chiseled_top");
		this.quartzblock_lines_top = par1IconRegister.registerIcon("quartzblock_lines_top");
		this.quartzblock_bottom = par1IconRegister.registerIcon("quartzblock_bottom");
	}
}
