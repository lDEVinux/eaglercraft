package net.minecraft.src;

public class ItemBlockWithMetadata extends ItemBlock {
	private Block theBlock;

	public ItemBlockWithMetadata(int par1, Block par2Block) {
		super(par1);
		this.theBlock = par2Block;
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	public Icon getIconFromDamage(int par1) {
		return this.theBlock.getIcon(2, par1);
	}

	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	public int getMetadata(int par1) {
		return par1;
	}
}
