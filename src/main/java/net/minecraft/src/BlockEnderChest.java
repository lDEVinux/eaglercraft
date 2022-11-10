package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockEnderChest extends BlockContainer {
	protected BlockEnderChest(int par1) {
		super(par1, Material.rock);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
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
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 22;
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Block.obsidian.blockID;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom par1Random) {
		return 8;
	}

	/**
	 * Return true if a player with Silk Touch can harvest this block directly, and
	 * not its normal drops.
	 */
	protected boolean canSilkHarvest() {
		return true;
	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack) {
		byte var7 = 0;
		int var8 = MathHelper.floor_double((double) (par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (var8 == 0) {
			var7 = 2;
		}

		if (var8 == 1) {
			var7 = 5;
		}

		if (var8 == 2) {
			var7 = 3;
		}

		if (var8 == 3) {
			var7 = 4;
		}

		par1World.setBlockMetadataWithNotify(par2, par3, par4, var7, 2);
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
		return new TileEntityEnderChest();
	}

	/**
	 * A randomly called display update to be able to add particles or other items
	 * for display
	 */
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		for (int var6 = 0; var6 < 3; ++var6) {
			double var10000 = (double) ((float) par2 + par5Random.nextFloat());
			double var9 = (double) ((float) par3 + par5Random.nextFloat());
			var10000 = (double) ((float) par4 + par5Random.nextFloat());
			double var13 = 0.0D;
			double var15 = 0.0D;
			double var17 = 0.0D;
			int var19 = par5Random.nextInt(2) * 2 - 1;
			int var20 = par5Random.nextInt(2) * 2 - 1;
			var13 = ((double) par5Random.nextFloat() - 0.5D) * 0.125D;
			var15 = ((double) par5Random.nextFloat() - 0.5D) * 0.125D;
			var17 = ((double) par5Random.nextFloat() - 0.5D) * 0.125D;
			double var11 = (double) par4 + 0.5D + 0.25D * (double) var20;
			var17 = (double) (par5Random.nextFloat() * 1.0F * (float) var20);
			double var7 = (double) par2 + 0.5D + 0.25D * (double) var19;
			var13 = (double) (par5Random.nextFloat() * 1.0F * (float) var19);
			par1World.spawnParticle("portal", var7, var9, var11, var13, var15, var17);
		}
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("obsidian");
	}
}
