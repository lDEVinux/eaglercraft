package net.minecraft.src;

public class BlockWood extends Block {
	/** The type of tree this block came from. */
	public static final String[] woodType = new String[] { "oak", "spruce", "birch", "jungle" };
	public static final String[] woodTextureTypes = new String[] { "wood", "wood_spruce", "wood_birch", "wood_jungle" };

	public BlockWood(int par1) {
		super(par1, Material.wood);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and wood.
	 */
	public int damageDropped(int par1) {
		return par1;
	}
}
