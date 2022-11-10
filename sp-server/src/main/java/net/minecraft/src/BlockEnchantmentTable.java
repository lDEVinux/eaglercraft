package net.minecraft.src;

public class BlockEnchantmentTable extends BlockContainer {
	protected BlockEnchantmentTable(int par1) {
		super(par1, Material.rock);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
		this.setLightOpacity(0);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether or
	 * not to render the shared face of two adjacent blocks and also whether the
	 * player can attach torches, redstone wire, etc to this block.
	 */
	public boolean isOpaqueCube() {
		return false;
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the
	 * block.
	 */
	public TileEntity createNewTileEntity(World par1World) {
		return new TileEntityEnchantmentTable();
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer,
			int par6, float par7, float par8, float par9) {
		if (par1World.isRemote) {
			return true;
		} else {
			TileEntityEnchantmentTable var10 = (TileEntityEnchantmentTable) par1World.getBlockTileEntity(par2, par3,
					par4);
			par5EntityPlayer.displayGUIEnchantment(par2, par3, par4,
					var10.func_94135_b() ? var10.func_94133_a() : null);
			return true;
		}
	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving,
			ItemStack par6ItemStack) {
		super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLiving, par6ItemStack);

		if (par6ItemStack.hasDisplayName()) {
			((TileEntityEnchantmentTable) par1World.getBlockTileEntity(par2, par3, par4))
					.func_94134_a(par6ItemStack.getDisplayName());
		}
	}
}
