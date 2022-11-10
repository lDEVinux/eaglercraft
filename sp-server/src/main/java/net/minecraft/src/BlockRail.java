package net.minecraft.src;

public class BlockRail extends BlockRailBase {
	protected BlockRail(int par1) {
		super(par1, false);
	}

	protected void func_94358_a(World par1World, int par2, int par3, int par4, int par5, int par6, int par7) {
		if (par7 > 0 && Block.blocksList[par7].canProvidePower()
				&& (new BlockBaseRailLogic(this, par1World, par2, par3, par4)).getNumberOfAdjacentTracks() == 3) {
			this.refreshTrackShape(par1World, par2, par3, par4, false);
		}
	}
}
