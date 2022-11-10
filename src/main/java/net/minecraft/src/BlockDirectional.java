package net.minecraft.src;

public abstract class BlockDirectional extends Block {
	protected BlockDirectional(int par1, Material par2Material) {
		super(par1, par2Material);
	}

	/**
	 * Returns the orentation value from the specified metadata
	 */
	public static int getDirection(int par0) {
		return par0 & 3;
	}
}
