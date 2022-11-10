package net.minecraft.src;

public class BlockSandStone extends Block {
	public static final String[] SAND_STONE_TYPES = new String[] { "default", "chiseled", "smooth" };
	private static final String[] field_94405_b = new String[] { "sandstone_side", "sandstone_carved",
			"sandstone_smooth" };

	public BlockSandStone(int par1) {
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
