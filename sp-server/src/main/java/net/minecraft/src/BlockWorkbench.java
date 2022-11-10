package net.minecraft.src;

public class BlockWorkbench extends Block {
	protected BlockWorkbench(int par1) {
		super(par1, Material.wood);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer,
			int par6, float par7, float par8, float par9) {
		if (par1World.isRemote) {
			return true;
		} else {
			par5EntityPlayer.displayGUIWorkbench(par2, par3, par4);
			return true;
		}
	}
}
