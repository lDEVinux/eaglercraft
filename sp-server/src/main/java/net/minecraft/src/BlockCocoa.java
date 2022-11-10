package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockCocoa extends BlockDirectional {
	public static final String[] cocoaIcons = new String[] { "cocoa_0", "cocoa_1", "cocoa_2" };

	public BlockCocoa(int par1) {
		super(par1, Material.plants);
		this.setTickRandomly(true);
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		if (!this.canBlockStay(par1World, par2, par3, par4)) {
			this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
			par1World.setBlockToAir(par2, par3, par4);
		} else if (par1World.rand.nextInt(5) == 0) {
			int var6 = par1World.getBlockMetadata(par2, par3, par4);
			int var7 = func_72219_c(var6);

			if (var7 < 2) {
				++var7;
				par1World.setBlockMetadata(par2, par3, par4, var7 << 2 | getDirection(var6), 2);
			}
		}
	}

	/**
	 * Can this block stay at this position. Similar to canPlaceBlockAt except gets
	 * checked often with plants.
	 */
	public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
		int var5 = getDirection(par1World.getBlockMetadata(par2, par3, par4));
		par2 += Direction.offsetX[var5];
		par4 += Direction.offsetZ[var5];
		int var6 = par1World.getBlockId(par2, par3, par4);
		return var6 == Block.wood.blockID
				&& BlockLog.limitToValidMetadata(par1World.getBlockMetadata(par2, par3, par4)) == 3;
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 28;
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
	 * Returns a bounding box from the pool of bounding boxes (this means this box
	 * can change after the pool has been cleared to be reused)
	 */
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
		return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		int var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
		int var6 = getDirection(var5);
		int var7 = func_72219_c(var5);
		int var8 = 4 + var7 * 2;
		int var9 = 5 + var7 * 2;
		float var10 = (float) var8 / 2.0F;

		switch (var6) {
		case 0:
			this.setBlockBounds((8.0F - var10) / 16.0F, (12.0F - (float) var9) / 16.0F, (15.0F - (float) var8) / 16.0F,
					(8.0F + var10) / 16.0F, 0.75F, 0.9375F);
			break;

		case 1:
			this.setBlockBounds(0.0625F, (12.0F - (float) var9) / 16.0F, (8.0F - var10) / 16.0F,
					(1.0F + (float) var8) / 16.0F, 0.75F, (8.0F + var10) / 16.0F);
			break;

		case 2:
			this.setBlockBounds((8.0F - var10) / 16.0F, (12.0F - (float) var9) / 16.0F, 0.0625F, (8.0F + var10) / 16.0F,
					0.75F, (1.0F + (float) var8) / 16.0F);
			break;

		case 3:
			this.setBlockBounds((15.0F - (float) var8) / 16.0F, (12.0F - (float) var9) / 16.0F, (8.0F - var10) / 16.0F,
					0.9375F, 0.75F, (8.0F + var10) / 16.0F);
		}
	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving,
			ItemStack par6ItemStack) {
		int var7 = ((MathHelper.floor_double((double) (par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) + 0)
				% 4;
		par1World.setBlockMetadata(par2, par3, par4, var7, 2);
	}

	/**
	 * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z,
	 * side, hitX, hitY, hitZ, block metadata
	 */
	public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7,
			float par8, int par9) {
		if (par5 == 1 || par5 == 0) {
			par5 = 2;
		}

		return Direction.footInvisibleFaceRemap[Direction.facingToDirection[par5]];
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * blockID
	 */
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		if (!this.canBlockStay(par1World, par2, par3, par4)) {
			this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
			par1World.setBlockToAir(par2, par3, par4);
		}
	}

	public static int func_72219_c(int par0) {
		return (par0 & 12) >> 2;
	}

	/**
	 * Drops the block items with a specified chance of dropping the specified items
	 */
	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6,
			int par7) {
		int var8 = func_72219_c(par5);
		byte var9 = 1;

		if (var8 >= 2) {
			var9 = 3;
		}

		for (int var10 = 0; var10 < var9; ++var10) {
			this.dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(Item.dyePowder, 1, 3));
		}
	}

	/**
	 * Get the block's damage value (for use with pick block).
	 */
	public int getDamageValue(World par1World, int par2, int par3, int par4) {
		return 3;
	}
}
