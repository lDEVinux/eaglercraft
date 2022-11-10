package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockPortal extends BlockBreakable {
	public BlockPortal(int par1) {
		super(par1, "portal", Material.portal, false);
		this.setTickRandomly(true);
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		super.updateTick(par1World, par2, par3, par4, par5Random);

		if (par1World.provider.isSurfaceWorld() && par5Random.nextInt(2000) < par1World.difficultySetting) {
			int var6;

			for (var6 = par3; !par1World.doesBlockHaveSolidTopSurface(par2, var6, par4) && var6 > 0; --var6) {
				;
			}

			if (var6 > 0 && !par1World.isBlockNormalCube(par2, var6 + 1, par4)) {
				Entity var7 = ItemMonsterPlacer.spawnCreature(par1World, 57, (double) par2 + 0.5D, (double) var6 + 1.1D,
						(double) par4 + 0.5D);

				if (var7 != null) {
					var7.timeUntilPortal = var7.getPortalCooldown();
				}
			}
		}
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this box
	 * can change after the pool has been cleared to be reused)
	 */
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		float var5;
		float var6;

		if (par1IBlockAccess.getBlockId(par2 - 1, par3, par4) != this.blockID
				&& par1IBlockAccess.getBlockId(par2 + 1, par3, par4) != this.blockID) {
			var5 = 0.125F;
			var6 = 0.5F;
			this.setBlockBounds(0.5F - var5, 0.0F, 0.5F - var6, 0.5F + var5, 1.0F, 0.5F + var6);
		} else {
			var5 = 0.5F;
			var6 = 0.125F;
			this.setBlockBounds(0.5F - var5, 0.0F, 0.5F - var6, 0.5F + var5, 1.0F, 0.5F + var6);
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
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
	 * Checks to see if this location is valid to create a portal and will return
	 * True if it does. Args: world, x, y, z
	 */
	public boolean tryToCreatePortal(World par1World, int par2, int par3, int par4) {
		byte var5 = 0;
		byte var6 = 0;

		if (par1World.getBlockId(par2 - 1, par3, par4) == Block.obsidian.blockID
				|| par1World.getBlockId(par2 + 1, par3, par4) == Block.obsidian.blockID) {
			var5 = 1;
		}

		if (par1World.getBlockId(par2, par3, par4 - 1) == Block.obsidian.blockID
				|| par1World.getBlockId(par2, par3, par4 + 1) == Block.obsidian.blockID) {
			var6 = 1;
		}

		if (var5 == var6) {
			return false;
		} else {
			if (par1World.getBlockId(par2 - var5, par3, par4 - var6) == 0) {
				par2 -= var5;
				par4 -= var6;
			}

			int var7;
			int var8;

			for (var7 = -1; var7 <= 2; ++var7) {
				for (var8 = -1; var8 <= 3; ++var8) {
					boolean var9 = var7 == -1 || var7 == 2 || var8 == -1 || var8 == 3;

					if (var7 != -1 && var7 != 2 || var8 != -1 && var8 != 3) {
						int var10 = par1World.getBlockId(par2 + var5 * var7, par3 + var8, par4 + var6 * var7);

						if (var9) {
							if (var10 != Block.obsidian.blockID) {
								return false;
							}
						} else if (var10 != 0 && var10 != Block.fire.blockID) {
							return false;
						}
					}
				}
			}

			for (var7 = 0; var7 < 2; ++var7) {
				for (var8 = 0; var8 < 3; ++var8) {
					par1World.setBlock(par2 + var5 * var7, par3 + var8, par4 + var6 * var7, Block.portal.blockID, 0, 2);
				}
			}

			return true;
		}
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * blockID
	 */
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		byte var6 = 0;
		byte var7 = 1;

		if (par1World.getBlockId(par2 - 1, par3, par4) == this.blockID
				|| par1World.getBlockId(par2 + 1, par3, par4) == this.blockID) {
			var6 = 1;
			var7 = 0;
		}

		int var8;

		for (var8 = par3; par1World.getBlockId(par2, var8 - 1, par4) == this.blockID; --var8) {
			;
		}

		if (par1World.getBlockId(par2, var8 - 1, par4) != Block.obsidian.blockID) {
			par1World.setBlockToAir(par2, par3, par4);
		} else {
			int var9;

			for (var9 = 1; var9 < 4 && par1World.getBlockId(par2, var8 + var9, par4) == this.blockID; ++var9) {
				;
			}

			if (var9 == 3 && par1World.getBlockId(par2, var8 + var9, par4) == Block.obsidian.blockID) {
				boolean var10 = par1World.getBlockId(par2 - 1, par3, par4) == this.blockID
						|| par1World.getBlockId(par2 + 1, par3, par4) == this.blockID;
				boolean var11 = par1World.getBlockId(par2, par3, par4 - 1) == this.blockID
						|| par1World.getBlockId(par2, par3, par4 + 1) == this.blockID;

				if (var10 && var11) {
					par1World.setBlockToAir(par2, par3, par4);
				} else {
					if ((par1World.getBlockId(par2 + var6, par3, par4 + var7) != Block.obsidian.blockID
							|| par1World.getBlockId(par2 - var6, par3, par4 - var7) != this.blockID)
							&& (par1World.getBlockId(par2 - var6, par3, par4 - var7) != Block.obsidian.blockID
									|| par1World.getBlockId(par2 + var6, par3, par4 + var7) != this.blockID)) {
						par1World.setBlockToAir(par2, par3, par4);
					}
				}
			} else {
				par1World.setBlockToAir(par2, par3, par4);
			}
		}
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom par1Random) {
		return 0;
	}

	/**
	 * Triggered whenever an entity collides with this block (enters into the
	 * block). Args: world, x, y, z, entity
	 */
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
		if (par5Entity.ridingEntity == null && par5Entity.riddenByEntity == null) {
			par5Entity.setInPortal();
		}
	}
}
