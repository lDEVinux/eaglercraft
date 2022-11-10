package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockPistonMoving extends BlockContainer {
	public BlockPistonMoving(int par1) {
		super(par1, Material.piston);
		this.setHardness(-1.0F);
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the
	 * block.
	 */
	public TileEntity createNewTileEntity(World par1World) {
		return null;
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
	}

	/**
	 * ejects contained items into the world, and notifies neighbours of an update,
	 * as appropriate
	 */
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		TileEntity var7 = par1World.getBlockTileEntity(par2, par3, par4);

		if (var7 instanceof TileEntityPiston) {
			((TileEntityPiston) var7).clearPistonTileEntity();
		} else {
			super.breakBlock(par1World, par2, par3, par4, par5, par6);
		}
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates.
	 * Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		return false;
	}

	/**
	 * checks to see if you can place this block can be placed on that side of a
	 * block: BlockLever overrides
	 */
	public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5) {
		return false;
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return -1;
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
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		return false;
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return 0;
	}

	/**
	 * Drops the block items with a specified chance of dropping the specified items
	 */
	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * blockID
	 */
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
	}

	/**
	 * gets a new TileEntityPiston created with the arguments provided.
	 */
	public static TileEntity getTileEntity(int par0, int par1, int par2, boolean par3, boolean par4) {
		return new TileEntityPiston(par0, par1, par2, par3, par4);
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this box
	 * can change after the pool has been cleared to be reused)
	 */
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		TileEntityPiston var5 = this.getTileEntityAtLocation(par1World, par2, par3, par4);

		if (var5 == null) {
			return null;
		} else {
			float var6 = var5.getProgress(0.0F);

			if (var5.isExtending()) {
				var6 = 1.0F - var6;
			}

			return this.getAxisAlignedBB(par1World, par2, par3, par4, var5.getStoredBlockID(), var6, var5.getPistonOrientation());
		}
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		TileEntityPiston var5 = this.getTileEntityAtLocation(par1IBlockAccess, par2, par3, par4);

		if (var5 != null) {
			Block var6 = Block.blocksList[var5.getStoredBlockID()];

			if (var6 == null || var6 == this) {
				return;
			}

			var6.setBlockBoundsBasedOnState(par1IBlockAccess, par2, par3, par4);
			float var7 = var5.getProgress(0.0F);

			if (var5.isExtending()) {
				var7 = 1.0F - var7;
			}

			int var8 = var5.getPistonOrientation();
			this.minX = var6.getBlockBoundsMinX() - (double) ((float) Facing.offsetsXForSide[var8] * var7);
			this.minY = var6.getBlockBoundsMinY() - (double) ((float) Facing.offsetsYForSide[var8] * var7);
			this.minZ = var6.getBlockBoundsMinZ() - (double) ((float) Facing.offsetsZForSide[var8] * var7);
			this.maxX = var6.getBlockBoundsMaxX() - (double) ((float) Facing.offsetsXForSide[var8] * var7);
			this.maxY = var6.getBlockBoundsMaxY() - (double) ((float) Facing.offsetsYForSide[var8] * var7);
			this.maxZ = var6.getBlockBoundsMaxZ() - (double) ((float) Facing.offsetsZForSide[var8] * var7);
		}
	}

	public AxisAlignedBB getAxisAlignedBB(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
		if (par5 != 0 && par5 != this.blockID) {
			AxisAlignedBB var8 = Block.blocksList[par5].getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);

			if (var8 == null) {
				return null;
			} else {
				if (Facing.offsetsXForSide[par7] < 0) {
					var8.minX -= (double) ((float) Facing.offsetsXForSide[par7] * par6);
				} else {
					var8.maxX -= (double) ((float) Facing.offsetsXForSide[par7] * par6);
				}

				if (Facing.offsetsYForSide[par7] < 0) {
					var8.minY -= (double) ((float) Facing.offsetsYForSide[par7] * par6);
				} else {
					var8.maxY -= (double) ((float) Facing.offsetsYForSide[par7] * par6);
				}

				if (Facing.offsetsZForSide[par7] < 0) {
					var8.minZ -= (double) ((float) Facing.offsetsZForSide[par7] * par6);
				} else {
					var8.maxZ -= (double) ((float) Facing.offsetsZForSide[par7] * par6);
				}

				return var8;
			}
		} else {
			return null;
		}
	}

	/**
	 * gets the piston tile entity at the specified location
	 */
	private TileEntityPiston getTileEntityAtLocation(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		TileEntity var5 = par1IBlockAccess.getBlockTileEntity(par2, par3, par4);
		return var5 instanceof TileEntityPiston ? (TileEntityPiston) var5 : null;
	}

	/**
	 * only called by clickMiddleMouseButton , and passed to
	 * inventory.setCurrentItem (along with isCreative)
	 */
	public int idPicked(World par1World, int par2, int par3, int par4) {
		return 0;
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("piston_top");
	}
}
