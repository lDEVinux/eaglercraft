package net.minecraft.src;

public class ItemAnvilBlock extends ItemMultiTextureTile {
	public ItemAnvilBlock(Block par1Block) {
		super(par1Block.blockID - 256, par1Block, BlockAnvil.statuses);
	}

	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	public int getMetadata(int par1) {
		return par1 << 2;
	}
}
