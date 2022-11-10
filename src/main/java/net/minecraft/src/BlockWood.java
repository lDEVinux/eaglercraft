package net.minecraft.src;

import java.util.List;

public class BlockWood extends Block {
	/** The type of tree this block came from. */
	public static final String[] woodType = new String[] { "oak", "spruce", "birch", "jungle" };
	public static final String[] woodTextureTypes = new String[] { "wood", "wood_spruce", "wood_birch", "wood_jungle" };
	private Icon[] iconArray;

	public BlockWood(int par1) {
		super(par1, Material.wood);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		if (par2 < 0 || par2 >= this.iconArray.length) {
			par2 = 0;
		}

		return this.iconArray[par2];
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and wood.
	 */
	public int damageDropped(int par1) {
		return par1;
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
		par3List.add(new ItemStack(par1, 1, 2));
		par3List.add(new ItemStack(par1, 1, 3));
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.iconArray = new Icon[woodTextureTypes.length];

		for (int var2 = 0; var2 < this.iconArray.length; ++var2) {
			this.iconArray[var2] = par1IconRegister.registerIcon(woodTextureTypes[var2]);
		}
	}
}
