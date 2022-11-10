package net.minecraft.src;

public interface ITileEntityProvider {
	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the
	 * block.
	 */
	TileEntity createNewTileEntity(World var1);
}
