package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockBookshelf extends Block {
	public BlockBookshelf(int par1) {
		super(par1, Material.wood);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		return par1 != 1 && par1 != 0 ? super.getIcon(par1, par2) : Block.planks.getBlockTextureFromSide(par1);
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom par1Random) {
		return 3;
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Item.book.itemID;
	}
}
