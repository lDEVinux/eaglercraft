package net.minecraft.src;

public class BlockQuartz extends Block {
	public static final String[] quartzBlockTypes = new String[] { "default", "chiseled", "lines" };
	private static final String[] quartzBlockTextureTypes = new String[] { "quartzblock_side", "quartzblock_chiseled",
			"quartzblock_lines", null, null };

	public BlockQuartz(int par1) {
		super(par1, Material.rock);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z,
	 * side, hitX, hitY, hitZ, block metadata
	 */
	public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7,
			float par8, int par9) {
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
}
