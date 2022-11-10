package net.minecraft.src;

public class BlockLeavesBase extends Block {
	/**
	 * Used to determine how to display leaves based on the graphics level. May also
	 * be used in rendering for transparency, not sure.
	 */
	protected boolean graphicsLevel;

	protected BlockLeavesBase(int par1, Material par2Material, boolean par3) {
		super(par1, par2Material);
		this.graphicsLevel = par3;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether or
	 * not to render the shared face of two adjacent blocks and also whether the
	 * player can attach torches, redstone wire, etc to this block.
	 */
	public boolean isOpaqueCube() {
		return false;
	}
}
