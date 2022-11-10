package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockTripWireSource extends Block {
	public BlockTripWireSource(int par1) {
		super(par1, Material.circuits);
		this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setTickRandomly(true);
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this box
	 * can change after the pool has been cleared to be reused)
	 */
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
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
		return 29;
	}

	/**
	 * How many world ticks before ticking
	 */
	public int tickRate(World par1World) {
		return 10;
	}

	/**
	 * checks to see if you can place this block can be placed on that side of a
	 * block: BlockLever overrides
	 */
	public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5) {
		return par5 == 2 && par1World.isBlockNormalCube(par2, par3, par4 + 1) ? true
				: (par5 == 3 && par1World.isBlockNormalCube(par2, par3, par4 - 1) ? true
						: (par5 == 4 && par1World.isBlockNormalCube(par2 + 1, par3, par4) ? true
								: par5 == 5 && par1World.isBlockNormalCube(par2 - 1, par3, par4)));
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates.
	 * Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		return par1World.isBlockNormalCube(par2 - 1, par3, par4) ? true
				: (par1World.isBlockNormalCube(par2 + 1, par3, par4) ? true
						: (par1World.isBlockNormalCube(par2, par3, par4 - 1) ? true
								: par1World.isBlockNormalCube(par2, par3, par4 + 1)));
	}

	/**
	 * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z,
	 * side, hitX, hitY, hitZ, block metadata
	 */
	public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7,
			float par8, int par9) {
		byte var10 = 0;

		if (par5 == 2 && par1World.isBlockNormalCubeDefault(par2, par3, par4 + 1, true)) {
			var10 = 2;
		}

		if (par5 == 3 && par1World.isBlockNormalCubeDefault(par2, par3, par4 - 1, true)) {
			var10 = 0;
		}

		if (par5 == 4 && par1World.isBlockNormalCubeDefault(par2 + 1, par3, par4, true)) {
			var10 = 1;
		}

		if (par5 == 5 && par1World.isBlockNormalCubeDefault(par2 - 1, par3, par4, true)) {
			var10 = 3;
		}

		return var10;
	}

	/**
	 * Called after a block is placed
	 */
	public void onPostBlockPlaced(World par1World, int par2, int par3, int par4, int par5) {
		this.func_72143_a(par1World, par2, par3, par4, this.blockID, par5, false, -1, 0);
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * blockID
	 */
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		if (par5 != this.blockID) {
			if (this.func_72144_l(par1World, par2, par3, par4)) {
				int var6 = par1World.getBlockMetadata(par2, par3, par4);
				int var7 = var6 & 3;
				boolean var8 = false;

				if (!par1World.isBlockNormalCube(par2 - 1, par3, par4) && var7 == 3) {
					var8 = true;
				}

				if (!par1World.isBlockNormalCube(par2 + 1, par3, par4) && var7 == 1) {
					var8 = true;
				}

				if (!par1World.isBlockNormalCube(par2, par3, par4 - 1) && var7 == 0) {
					var8 = true;
				}

				if (!par1World.isBlockNormalCube(par2, par3, par4 + 1) && var7 == 2) {
					var8 = true;
				}

				if (var8) {
					this.dropBlockAsItem(par1World, par2, par3, par4, var6, 0);
					par1World.setBlockToAir(par2, par3, par4);
				}
			}
		}
	}

	public void func_72143_a(World par1World, int par2, int par3, int par4, int par5, int par6, boolean par7, int par8,
			int par9) {
		int var10 = par6 & 3;
		boolean var11 = (par6 & 4) == 4;
		boolean var12 = (par6 & 8) == 8;
		boolean var13 = par5 == Block.tripWireSource.blockID;
		boolean var14 = false;
		boolean var15 = !par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4);
		int var16 = Direction.offsetX[var10];
		int var17 = Direction.offsetZ[var10];
		int var18 = 0;
		int[] var19 = new int[42];
		int var20;
		int var21;
		int var22;
		int var23;
		int var24;

		for (var20 = 1; var20 < 42; ++var20) {
			var21 = par2 + var16 * var20;
			var22 = par4 + var17 * var20;
			var23 = par1World.getBlockId(var21, par3, var22);

			if (var23 == Block.tripWireSource.blockID) {
				var24 = par1World.getBlockMetadata(var21, par3, var22);

				if ((var24 & 3) == Direction.footInvisibleFaceRemap[var10]) {
					var18 = var20;
				}

				break;
			}

			if (var23 != Block.tripWire.blockID && var20 != par8) {
				var19[var20] = -1;
				var13 = false;
			} else {
				var24 = var20 == par8 ? par9 : par1World.getBlockMetadata(var21, par3, var22);
				boolean var25 = (var24 & 8) != 8;
				boolean var26 = (var24 & 1) == 1;
				boolean var27 = (var24 & 2) == 2;
				var13 &= var27 == var15;
				var14 |= var25 && var26;
				var19[var20] = var24;

				if (var20 == par8) {
					par1World.scheduleBlockUpdate(par2, par3, par4, par5, this.tickRate(par1World));
					var13 &= var25;
				}
			}
		}

		var13 &= var18 > 1;
		var14 &= var13;
		var20 = (var13 ? 4 : 0) | (var14 ? 8 : 0);
		par6 = var10 | var20;

		if (var18 > 0) {
			var21 = par2 + var16 * var18;
			var22 = par4 + var17 * var18;
			var23 = Direction.footInvisibleFaceRemap[var10];
			par1World.setBlockMetadata(var21, par3, var22, var23 | var20, 3);
			this.notifyNeighborOfChange(par1World, var21, par3, var22, var23);
			this.playSoundEffect(par1World, var21, par3, var22, var13, var14, var11, var12);
		}

		this.playSoundEffect(par1World, par2, par3, par4, var13, var14, var11, var12);

		if (par5 > 0) {
			par1World.setBlockMetadata(par2, par3, par4, par6, 3);

			if (par7) {
				this.notifyNeighborOfChange(par1World, par2, par3, par4, var10);
			}
		}

		if (var11 != var13) {
			for (var21 = 1; var21 < var18; ++var21) {
				var22 = par2 + var16 * var21;
				var23 = par4 + var17 * var21;
				var24 = var19[var21];

				if (var24 >= 0) {
					if (var13) {
						var24 |= 4;
					} else {
						var24 &= -5;
					}

					par1World.setBlockMetadata(var22, par3, var23, var24, 3);
				}
			}
		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		this.func_72143_a(par1World, par2, par3, par4, this.blockID, par1World.getBlockMetadata(par2, par3, par4), true,
				-1, 0);
	}

	/**
	 * only of the conditions are right
	 */
	private void playSoundEffect(World par1World, int par2, int par3, int par4, boolean par5, boolean par6,
			boolean par7, boolean par8) {
		if (par6 && !par8) {
			par1World.playSoundEffect((double) par2 + 0.5D, (double) par3 + 0.1D, (double) par4 + 0.5D, "random.click",
					0.4F, 0.6F);
		} else if (!par6 && par8) {
			par1World.playSoundEffect((double) par2 + 0.5D, (double) par3 + 0.1D, (double) par4 + 0.5D, "random.click",
					0.4F, 0.5F);
		} else if (par5 && !par7) {
			par1World.playSoundEffect((double) par2 + 0.5D, (double) par3 + 0.1D, (double) par4 + 0.5D, "random.click",
					0.4F, 0.7F);
		} else if (!par5 && par7) {
			par1World.playSoundEffect((double) par2 + 0.5D, (double) par3 + 0.1D, (double) par4 + 0.5D, "random.bowhit",
					0.4F, 1.2F / (par1World.rand.nextFloat() * 0.2F + 0.9F));
		}
	}

	private void notifyNeighborOfChange(World par1World, int par2, int par3, int par4, int par5) {
		par1World.notifyBlocksOfNeighborChange(par2, par3, par4, this.blockID);

		if (par5 == 3) {
			par1World.notifyBlocksOfNeighborChange(par2 - 1, par3, par4, this.blockID);
		} else if (par5 == 1) {
			par1World.notifyBlocksOfNeighborChange(par2 + 1, par3, par4, this.blockID);
		} else if (par5 == 0) {
			par1World.notifyBlocksOfNeighborChange(par2, par3, par4 - 1, this.blockID);
		} else if (par5 == 2) {
			par1World.notifyBlocksOfNeighborChange(par2, par3, par4 + 1, this.blockID);
		}
	}

	private boolean func_72144_l(World par1World, int par2, int par3, int par4) {
		if (!this.canPlaceBlockAt(par1World, par2, par3, par4)) {
			this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
			par1World.setBlockToAir(par2, par3, par4);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		int var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 3;
		float var6 = 0.1875F;

		if (var5 == 3) {
			this.setBlockBounds(0.0F, 0.2F, 0.5F - var6, var6 * 2.0F, 0.8F, 0.5F + var6);
		} else if (var5 == 1) {
			this.setBlockBounds(1.0F - var6 * 2.0F, 0.2F, 0.5F - var6, 1.0F, 0.8F, 0.5F + var6);
		} else if (var5 == 0) {
			this.setBlockBounds(0.5F - var6, 0.2F, 0.0F, 0.5F + var6, 0.8F, var6 * 2.0F);
		} else if (var5 == 2) {
			this.setBlockBounds(0.5F - var6, 0.2F, 1.0F - var6 * 2.0F, 0.5F + var6, 0.8F, 1.0F);
		}
	}

	/**
	 * ejects contained items into the world, and notifies neighbours of an update,
	 * as appropriate
	 */
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		boolean var7 = (par6 & 4) == 4;
		boolean var8 = (par6 & 8) == 8;

		if (var7 || var8) {
			this.func_72143_a(par1World, par2, par3, par4, 0, par6, false, -1, 0);
		}

		if (var8) {
			par1World.notifyBlocksOfNeighborChange(par2, par3, par4, this.blockID);
			int var9 = par6 & 3;

			if (var9 == 3) {
				par1World.notifyBlocksOfNeighborChange(par2 - 1, par3, par4, this.blockID);
			} else if (var9 == 1) {
				par1World.notifyBlocksOfNeighborChange(par2 + 1, par3, par4, this.blockID);
			} else if (var9 == 0) {
				par1World.notifyBlocksOfNeighborChange(par2, par3, par4 - 1, this.blockID);
			} else if (var9 == 2) {
				par1World.notifyBlocksOfNeighborChange(par2, par3, par4 + 1, this.blockID);
			}
		}

		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	/**
	 * Returns true if the block is emitting indirect/weak redstone power on the
	 * specified side. If isBlockNormalCube returns true, standard redstone
	 * propagation rules will apply instead and this will not be called. Args:
	 * World, X, Y, Z, side. Note that the side is reversed - eg it is 1 (up) when
	 * checking the bottom of the block.
	 */
	public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return (par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 8) == 8 ? 15 : 0;
	}

	/**
	 * Returns true if the block is emitting direct/strong redstone power on the
	 * specified side. Args: World, X, Y, Z, side. Note that the side is reversed -
	 * eg it is 1 (up) when checking the bottom of the block.
	 */
	public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		int var6 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);

		if ((var6 & 8) != 8) {
			return 0;
		} else {
			int var7 = var6 & 3;
			return var7 == 2 && par5 == 2 ? 15
					: (var7 == 0 && par5 == 3 ? 15 : (var7 == 1 && par5 == 4 ? 15 : (var7 == 3 && par5 == 5 ? 15 : 0)));
		}
	}

	/**
	 * Can this block provide power. Only wire currently seems to have this change
	 * based on its state.
	 */
	public boolean canProvidePower() {
		return true;
	}
}
