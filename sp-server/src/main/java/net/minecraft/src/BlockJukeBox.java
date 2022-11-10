package net.minecraft.src;

public class BlockJukeBox extends BlockContainer {
	protected BlockJukeBox(int par1) {
		super(par1, Material.wood);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer,
			int par6, float par7, float par8, float par9) {
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
		if (!par1World.isRemote) {
			TileEntityRecordPlayer var6 = (TileEntityRecordPlayer) par1World.getBlockTileEntity(par2, par3, par4);

			if (var6 != null) {
				var6.func_96098_a(par5ItemStack.copy());
				par1World.setBlockMetadata(par2, par3, par4, 1, 2);
			}
		}
	}

	/**
	 * Ejects the current record inside of the jukebox.
	 */
	public void ejectRecord(World par1World, int par2, int par3, int par4) {
		if (!par1World.isRemote) {
			TileEntityRecordPlayer var5 = (TileEntityRecordPlayer) par1World.getBlockTileEntity(par2, par3, par4);

			if (var5 != null) {
				ItemStack var6 = var5.func_96097_a();

				if (var6 != null) {
					par1World.playAuxSFX(1005, par2, par3, par4, 0);
					par1World.playRecord((String) null, par2, par3, par4);
					var5.func_96098_a((ItemStack) null);
					par1World.setBlockMetadata(par2, par3, par4, 0, 2);
					float var7 = 0.7F;
					double var8 = (double) (par1World.rand.nextFloat() * var7) + (double) (1.0F - var7) * 0.5D;
					double var10 = (double) (par1World.rand.nextFloat() * var7) + (double) (1.0F - var7) * 0.2D + 0.6D;
					double var12 = (double) (par1World.rand.nextFloat() * var7) + (double) (1.0F - var7) * 0.5D;
					ItemStack var14 = var6.copy();
					EntityItem var15 = new EntityItem(par1World, (double) par2 + var8, (double) par3 + var10,
							(double) par4 + var12, var14);
					var15.delayBeforeCanPickup = 10;
					par1World.spawnEntityInWorld(var15);
				}
			}
		}
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
	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6,
			int par7) {
		if (!par1World.isRemote) {
			super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, 0);
		}
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the
	 * block.
	 */
	public TileEntity createNewTileEntity(World par1World) {
		return new TileEntityRecordPlayer();
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
