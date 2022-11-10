package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockFarmland extends Block {
	private Icon field_94441_a;
	private Icon field_94440_b;

	protected BlockFarmland(int par1) {
		super(par1, Material.ground);
		this.setTickRandomly(true);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
		this.setLightOpacity(255);
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this box
	 * can change after the pool has been cleared to be reused)
	 */
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return AxisAlignedBB.getAABBPool().getAABB((double) (par2 + 0), (double) (par3 + 0), (double) (par4 + 0), (double) (par2 + 1), (double) (par3 + 1), (double) (par4 + 1));
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
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		return par1 == 1 ? (par2 > 0 ? this.field_94441_a : this.field_94440_b) : Block.dirt.getBlockTextureFromSide(par1);
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		if (!this.isWaterNearby(par1World, par2, par3, par4) && !par1World.canLightningStrikeAt(par2, par3 + 1, par4)) {
			int var6 = par1World.getBlockMetadata(par2, par3, par4);

			if (var6 > 0) {
				par1World.setBlockMetadataWithNotify(par2, par3, par4, var6 - 1, 2);
			} else if (!this.isCropsNearby(par1World, par2, par3, par4)) {
				par1World.setBlock(par2, par3, par4, Block.dirt.blockID);
			}
		} else {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 7, 2);
		}
	}

	/**
	 * returns true if there is at least one cropblock nearby (x-1 to x+1, y+1, z-1
	 * to z+1)
	 */
	private boolean isCropsNearby(World par1World, int par2, int par3, int par4) {
		byte var5 = 0;

		for (int var6 = par2 - var5; var6 <= par2 + var5; ++var6) {
			for (int var7 = par4 - var5; var7 <= par4 + var5; ++var7) {
				int var8 = par1World.getBlockId(var6, par3 + 1, var7);

				if (var8 == Block.crops.blockID || var8 == Block.melonStem.blockID || var8 == Block.pumpkinStem.blockID || var8 == Block.potato.blockID || var8 == Block.carrot.blockID) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * returns true if there's water nearby (x-4 to x+4, y to y+1, k-4 to k+4)
	 */
	private boolean isWaterNearby(World par1World, int par2, int par3, int par4) {
		for (int var5 = par2 - 4; var5 <= par2 + 4; ++var5) {
			for (int var6 = par3; var6 <= par3 + 1; ++var6) {
				for (int var7 = par4 - 4; var7 <= par4 + 4; ++var7) {
					if (par1World.getBlockMaterial(var5, var6, var7) == Material.water) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * blockID
	 */
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
		Material var6 = par1World.getBlockMaterial(par2, par3 + 1, par4);

		if (var6.isSolid()) {
			par1World.setBlock(par2, par3, par4, Block.dirt.blockID);
		}
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Block.dirt.idDropped(0, par2Random, par3);
	}

	/**
	 * only called by clickMiddleMouseButton , and passed to
	 * inventory.setCurrentItem (along with isCreative)
	 */
	public int idPicked(World par1World, int par2, int par3, int par4) {
		return Block.dirt.blockID;
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.field_94441_a = par1IconRegister.registerIcon("farmland_wet");
		this.field_94440_b = par1IconRegister.registerIcon("farmland_dry");
	}
}
