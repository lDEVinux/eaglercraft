package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockEnchantmentTable extends BlockContainer {
	private Icon field_94461_a;
	private Icon field_94460_b;

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
	 * A randomly called display update to be able to add particles or other items
	 * for display
	 */
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		super.randomDisplayTick(par1World, par2, par3, par4, par5Random);

		for (int var6 = par2 - 2; var6 <= par2 + 2; ++var6) {
			for (int var7 = par4 - 2; var7 <= par4 + 2; ++var7) {
				if (var6 > par2 - 2 && var6 < par2 + 2 && var7 == par4 - 1) {
					var7 = par4 + 2;
				}

				if (par5Random.nextInt(16) == 0) {
					for (int var8 = par3; var8 <= par3 + 1; ++var8) {
						if (par1World.getBlockId(var6, var8, var7) == Block.bookShelf.blockID) {
							if (!par1World.isAirBlock((var6 - par2) / 2 + par2, var8, (var7 - par4) / 2 + par4)) {
								break;
							}

							par1World.spawnParticle("enchantmenttable", (double) par2 + 0.5D, (double) par3 + 2.0D, (double) par4 + 0.5D, (double) ((float) (var6 - par2) + par5Random.nextFloat()) - 0.5D,
									(double) ((float) (var8 - par3) - par5Random.nextFloat() - 1.0F), (double) ((float) (var7 - par4) + par5Random.nextFloat()) - 0.5D);
						}
					}
				}
			}
		}
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
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		return par1 == 0 ? this.field_94460_b : (par1 == 1 ? this.field_94461_a : this.blockIcon);
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
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		return true;
	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack) {
		super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLiving, par6ItemStack);

		if (par6ItemStack.hasDisplayName()) {
			((TileEntityEnchantmentTable) par1World.getBlockTileEntity(par2, par3, par4)).func_94134_a(par6ItemStack.getDisplayName());
		}
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("enchantment_side");
		this.field_94461_a = par1IconRegister.registerIcon("enchantment_top");
		this.field_94460_b = par1IconRegister.registerIcon("enchantment_bottom");
	}
}
