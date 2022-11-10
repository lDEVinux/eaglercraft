package net.minecraft.src;

public class BlockJukeBox extends BlockContainer {
	private Icon theIcon;

	protected BlockJukeBox(int par1) {
		super(par1, Material.wood);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		return par1 == 1 ? this.theIcon : this.blockIcon;
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		if (par1World.getBlockMetadata(par2, par3, par4) == 0) {
			return false;
		} else {
			this.ejectRecord(par1World, par2, par3, par4);
			return true;
		}
	}

	/**
	 * Insert the specified music disc in the jukebox at the given coordinates
	 */
	public void insertRecord(World par1World, int par2, int par3, int par4, ItemStack par5ItemStack) {
	}

	/**
	 * Ejects the current record inside of the jukebox.
	 */
	public void ejectRecord(World par1World, int par2, int par3, int par4) {
	}

	/**
	 * ejects contained items into the world, and notifies neighbours of an update,
	 * as appropriate
	 */
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		this.ejectRecord(par1World, par2, par3, par4);
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	/**
	 * Drops the block items with a specified chance of dropping the specified items
	 */
	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the
	 * block.
	 */
	public TileEntity createNewTileEntity(World par1World) {
		return new TileEntityRecordPlayer();
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("musicBlock");
		this.theIcon = par1IconRegister.registerIcon("jukebox_top");
	}

	/**
	 * If this returns true, then comparators facing away from this block will use
	 * the value from getComparatorInputOverride instead of the actual redstone
	 * signal strength.
	 */
	public boolean hasComparatorInputOverride() {
		return true;
	}

	/**
	 * If hasComparatorInputOverride returns true, the return value from this is
	 * used instead of the redstone signal strength when this block inputs to a
	 * comparator.
	 */
	public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
		ItemStack var6 = ((TileEntityRecordPlayer) par1World.getBlockTileEntity(par2, par3, par4)).func_96097_a();
		return var6 == null ? 0 : var6.itemID + 1 - Item.record13.itemID;
	}
}
