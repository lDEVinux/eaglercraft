package net.minecraft.src;

public class BlockButtonWood extends BlockButton {
	protected BlockButtonWood(int par1) {
		super(par1, true);
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		return Block.planks.getBlockTextureFromSide(1);
	}
}
