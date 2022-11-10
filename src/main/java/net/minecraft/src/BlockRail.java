package net.minecraft.src;

public class BlockRail extends BlockRailBase {
	private Icon theIcon;

	protected BlockRail(int par1) {
		super(par1, false);
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		return par2 >= 6 ? this.theIcon : this.blockIcon;
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		super.registerIcons(par1IconRegister);
		this.theIcon = par1IconRegister.registerIcon("rail_turn");
	}

	protected void func_94358_a(World par1World, int par2, int par3, int par4, int par5, int par6, int par7) {
		if (par7 > 0 && Block.blocksList[par7].canProvidePower() && (new BlockBaseRailLogic(this, par1World, par2, par3, par4)).getNumberOfAdjacentTracks() == 3) {
			this.refreshTrackShape(par1World, par2, par3, par4, false);
		}
	}
}
