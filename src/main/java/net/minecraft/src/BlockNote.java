package net.minecraft.src;



public class BlockNote extends BlockContainer {
	public BlockNote(int par1) {
		super(par1, Material.wood);
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * blockID
	 */
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		boolean var6 = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
		TileEntityNote var7 = (TileEntityNote) par1World.getBlockTileEntity(par2, par3, par4);

		if (var7 != null && var7.previousRedstoneState != var6) {
			if (var6) {
				var7.triggerNote(par1World, par2, par3, par4);
			}

			var7.previousRedstoneState = var6;
		}
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		return true;
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the
	 * block.
	 */
	public TileEntity createNewTileEntity(World par1World) {
		return new TileEntityNote();
	}

	/**
	 * Called when the block receives a BlockEvent - see World.addBlockEvent. By
	 * default, passes it on to the tile entity at this location. Args: world, x, y,
	 * z, blockID, EventID, event parameter
	 */
	public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
		float var7 = (float) Math.pow(2.0D, (double) (par6 - 12) / 12.0D);
		String var8 = "harp";

		if (par5 == 1) {
			var8 = "bd";
		}

		if (par5 == 2) {
			var8 = "snare";
		}

		if (par5 == 3) {
			var8 = "hat";
		}

		if (par5 == 4) {
			var8 = "bassattack";
		}

		par1World.playSoundEffect((double) par2 + 0.5D, (double) par3 + 0.5D, (double) par4 + 0.5D, "note." + var8, 3.0F, var7);
		par1World.spawnParticle("note", (double) par2 + 0.5D, (double) par3 + 1.2D, (double) par4 + 0.5D, (double) par6 / 24.0D, 0.0D, 0.0D);
		return true;
	}
}
