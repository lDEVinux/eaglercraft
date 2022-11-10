package net.minecraft.src;

public class BlockStoneBrick extends Block {
	public static final String[] STONE_BRICK_TYPES = new String[] { "default", "mossy", "cracked", "chiseled" };
	public static final String[] field_94407_b = new String[] { "stonebricksmooth", "stonebricksmooth_mossy",
			"stonebricksmooth_cracked", "stonebricksmooth_carved" };

	public BlockStoneBrick(int par1) {
		super(par1, Material.rock);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and wood.
	 */
	public int damageDropped(int par1) {
		return par1;
	}
}
