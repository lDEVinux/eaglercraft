package net.minecraft.src;

import java.util.List;

public class BlockPistonBase extends Block {
	/** This pistons is the sticky one? */
	private final boolean isSticky;

	public BlockPistonBase(int par1, boolean par2) {
		super(par1, Material.piston);
		this.isSticky = par2;
		this.setStepSound(soundStoneFootstep);
		this.setHardness(0.5F);
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 16;
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
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer,
			int par6, float par7, float par8, float par9) {
		return false;
	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving,
			ItemStack par6ItemStack) {
		int var7 = determineOrientation(par1World, par2, par3, par4, par5EntityLiving);
		par1World.setBlockMetadata(par2, par3, par4, var7, 2);

		if (!par1World.isRemote) {
			this.updatePistonState(par1World, par2, par3, par4);
		}
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * blockID
	 */
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		if (!par1World.isRemote) {
			this.updatePistonState(par1World, par2, par3, par4);
		}
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		if (!par1World.isRemote && par1World.getBlockTileEntity(par2, par3, par4) == null) {
			this.updatePistonState(par1World, par2, par3, par4);
		}
	}

	/**
	 * handles attempts to extend or retract the piston.
	 */
	private void updatePistonState(World par1World, int par2, int par3, int par4) {
		int var5 = par1World.getBlockMetadata(par2, par3, par4);
		int var6 = getOrientation(var5);

		if (var6 != 7) {
			boolean var7 = this.isIndirectlyPowered(par1World, par2, par3, par4, var6);

			if (var7 && !isExtended(var5)) {
				if (canExtend(par1World, par2, par3, par4, var6)) {
					par1World.addBlockEvent(par2, par3, par4, this.blockID, 0, var6);
				}
			} else if (!var7 && isExtended(var5)) {
				par1World.setBlockMetadata(par2, par3, par4, var6, 2);
				par1World.addBlockEvent(par2, par3, par4, this.blockID, 1, var6);
			}
		}
	}

	/**
	 * checks the block to that side to see if it is indirectly powered.
	 */
	private boolean isIndirectlyPowered(World par1World, int par2, int par3, int par4, int par5) {
		return par5 != 0 && par1World.getIndirectPowerOutput(par2, par3 - 1, par4, 0) ? true
				: (par5 != 1 && par1World.getIndirectPowerOutput(par2, par3 + 1, par4, 1) ? true
						: (par5 != 2 && par1World.getIndirectPowerOutput(par2, par3, par4 - 1, 2) ? true
								: (par5 != 3 && par1World.getIndirectPowerOutput(par2, par3, par4 + 1, 3) ? true
										: (par5 != 5 && par1World.getIndirectPowerOutput(par2 + 1, par3, par4, 5) ? true
												: (par5 != 4
														&& par1World.getIndirectPowerOutput(par2 - 1, par3, par4, 4)
																? true
																: (par1World.getIndirectPowerOutput(par2, par3, par4, 0)
																		? true
																		: (par1World.getIndirectPowerOutput(par2,
																				par3 + 2, par4, 1)
																						? true
																						: (par1World
																								.getIndirectPowerOutput(
																										par2, par3 + 1,
																										par4 - 1, 2)
																												? true
																												: (par1World
																														.getIndirectPowerOutput(
																																par2,
																																par3 + 1,
																																par4 + 1,
																																3) ? true
																																		: (par1World
																																				.getIndirectPowerOutput(
																																						par2 - 1,
																																						par3 + 1,
																																						par4,
																																						4) ? true
																																								: par1World
																																										.getIndirectPowerOutput(
																																												par2 + 1,
																																												par3 + 1,
																																												par4,
																																												5)))))))))));
	}

	/**
	 * Called when the block receives a BlockEvent - see World.addBlockEvent. By
	 * default, passes it on to the tile entity at this location. Args: world, x, y,
	 * z, blockID, EventID, event parameter
	 */
	public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
		if (!par1World.isRemote) {
			boolean var7 = this.isIndirectlyPowered(par1World, par2, par3, par4, par6);

			if (var7 && par5 == 1) {
				par1World.setBlockMetadata(par2, par3, par4, par6 | 8, 2);
				return false;
			}

			if (!var7 && par5 == 0) {
				return false;
			}
		}

		if (par5 == 0) {
			if (!this.tryExtend(par1World, par2, par3, par4, par6)) {
				return false;
			}

			par1World.setBlockMetadata(par2, par3, par4, par6 | 8, 2);
			par1World.playSoundEffect((double) par2 + 0.5D, (double) par3 + 0.5D, (double) par4 + 0.5D,
					"tile.piston.out", 0.5F, par1World.rand.nextFloat() * 0.25F + 0.6F);
		} else if (par5 == 1) {
			TileEntity var16 = par1World.getBlockTileEntity(par2 + Facing.offsetsXForSide[par6],
					par3 + Facing.offsetsYForSide[par6], par4 + Facing.offsetsZForSide[par6]);

			if (var16 instanceof TileEntityPiston) {
				((TileEntityPiston) var16).clearPistonTileEntity();
			}

			par1World.setBlock(par2, par3, par4, Block.pistonMoving.blockID, par6, 3);
			par1World.setBlockTileEntity(par2, par3, par4,
					BlockPistonMoving.getTileEntity(this.blockID, par6, par6, false, true));

			if (this.isSticky) {
				int var8 = par2 + Facing.offsetsXForSide[par6] * 2;
				int var9 = par3 + Facing.offsetsYForSide[par6] * 2;
				int var10 = par4 + Facing.offsetsZForSide[par6] * 2;
				int var11 = par1World.getBlockId(var8, var9, var10);
				int var12 = par1World.getBlockMetadata(var8, var9, var10);
				boolean var13 = false;

				if (var11 == Block.pistonMoving.blockID) {
					TileEntity var14 = par1World.getBlockTileEntity(var8, var9, var10);

					if (var14 instanceof TileEntityPiston) {
						TileEntityPiston var15 = (TileEntityPiston) var14;

						if (var15.getPistonOrientation() == par6 && var15.isExtending()) {
							var15.clearPistonTileEntity();
							var11 = var15.getStoredBlockID();
							var12 = var15.getBlockMetadata();
							var13 = true;
						}
					}
				}

				if (!var13 && var11 > 0 && canPushBlock(var11, par1World, var8, var9, var10, false)
						&& (Block.blocksList[var11].getMobilityFlag() == 0 || var11 == Block.pistonBase.blockID
								|| var11 == Block.pistonStickyBase.blockID)) {
					par2 += Facing.offsetsXForSide[par6];
					par3 += Facing.offsetsYForSide[par6];
					par4 += Facing.offsetsZForSide[par6];
					par1World.setBlock(par2, par3, par4, Block.pistonMoving.blockID, var12, 3);
					par1World.setBlockTileEntity(par2, par3, par4,
							BlockPistonMoving.getTileEntity(var11, var12, par6, false, false));
					par1World.setBlockToAir(var8, var9, var10);
				} else if (!var13) {
					par1World.setBlockToAir(par2 + Facing.offsetsXForSide[par6], par3 + Facing.offsetsYForSide[par6],
							par4 + Facing.offsetsZForSide[par6]);
				}
			} else {
				par1World.setBlockToAir(par2 + Facing.offsetsXForSide[par6], par3 + Facing.offsetsYForSide[par6],
						par4 + Facing.offsetsZForSide[par6]);
			}

			par1World.playSoundEffect((double) par2 + 0.5D, (double) par3 + 0.5D, (double) par4 + 0.5D,
					"tile.piston.in", 0.5F, par1World.rand.nextFloat() * 0.15F + 0.6F);
		}

		return true;
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		int var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);

		if (isExtended(var5)) {
			switch (getOrientation(var5)) {
			case 0:
				this.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;

			case 1:
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
				break;

			case 2:
				this.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
				break;

			case 3:
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
				break;

			case 4:
				this.setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;

			case 5:
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
			}
		} else {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	/**
	 * Sets the block's bounds for rendering it as an item
	 */
	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Adds all intersecting collision boxes to a list. (Be sure to only add boxes
	 * to the list if they intersect the mask.) Parameters: World, X, Y, Z, mask,
	 * list, colliding entity
	 */
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB,
			List par6List, Entity par7Entity) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
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
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
	 * returns an int which describes the direction the piston faces
	 */
	public static int getOrientation(int par0) {
		return par0 & 7;
	}

	/**
	 * Determine if the metadata is related to something powered.
	 */
	public static boolean isExtended(int par0) {
		return (par0 & 8) != 0;
	}

	/**
	 * gets the way this piston should face for that entity that placed it.
	 */
	public static int determineOrientation(World par0World, int par1, int par2, int par3,
			EntityLiving par4EntityLiving) {
		if (MathHelper.abs((float) par4EntityLiving.posX - (float) par1) < 2.0F
				&& MathHelper.abs((float) par4EntityLiving.posZ - (float) par3) < 2.0F) {
			double var5 = par4EntityLiving.posY + 1.82D - (double) par4EntityLiving.yOffset;

			if (var5 - (double) par2 > 2.0D) {
				return 1;
			}

			if ((double) par2 - var5 > 0.0D) {
				return 0;
			}
		}

		int var7 = MathHelper.floor_double((double) (par4EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		return var7 == 0 ? 2 : (var7 == 1 ? 5 : (var7 == 2 ? 3 : (var7 == 3 ? 4 : 0)));
	}

	/**
	 * returns true if the piston can push the specified block
	 */
	private static boolean canPushBlock(int par0, World par1World, int par2, int par3, int par4, boolean par5) {
		if (par0 == Block.obsidian.blockID) {
			return false;
		} else {
			if (par0 != Block.pistonBase.blockID && par0 != Block.pistonStickyBase.blockID) {
				if (Block.blocksList[par0].getBlockHardness(par1World, par2, par3, par4) == -1.0F) {
					return false;
				}

				if (Block.blocksList[par0].getMobilityFlag() == 2) {
					return false;
				}

				if (Block.blocksList[par0].getMobilityFlag() == 1) {
					if (!par5) {
						return false;
					}

					return true;
				}
			} else if (isExtended(par1World.getBlockMetadata(par2, par3, par4))) {
				return false;
			}

			return !(Block.blocksList[par0] instanceof ITileEntityProvider);
		}
	}

	/**
	 * checks to see if this piston could push the blocks in front of it.
	 */
	private static boolean canExtend(World par0World, int par1, int par2, int par3, int par4) {
		int var5 = par1 + Facing.offsetsXForSide[par4];
		int var6 = par2 + Facing.offsetsYForSide[par4];
		int var7 = par3 + Facing.offsetsZForSide[par4];
		int var8 = 0;

		while (true) {
			if (var8 < 13) {
				if (var6 <= 0 || var6 >= 255) {
					return false;
				}

				int var9 = par0World.getBlockId(var5, var6, var7);

				if (var9 != 0) {
					if (!canPushBlock(var9, par0World, var5, var6, var7, true)) {
						return false;
					}

					if (Block.blocksList[var9].getMobilityFlag() != 1) {
						if (var8 == 12) {
							return false;
						}

						var5 += Facing.offsetsXForSide[par4];
						var6 += Facing.offsetsYForSide[par4];
						var7 += Facing.offsetsZForSide[par4];
						++var8;
						continue;
					}
				}
			}

			return true;
		}
	}

	/**
	 * attempts to extend the piston. returns false if impossible.
	 */
	private boolean tryExtend(World par1World, int par2, int par3, int par4, int par5) {
		int var6 = par2 + Facing.offsetsXForSide[par5];
		int var7 = par3 + Facing.offsetsYForSide[par5];
		int var8 = par4 + Facing.offsetsZForSide[par5];
		int var9 = 0;

		while (true) {
			int var10;

			if (var9 < 13) {
				if (var7 <= 0 || var7 >= 255) {
					return false;
				}

				var10 = par1World.getBlockId(var6, var7, var8);

				if (var10 != 0) {
					if (!canPushBlock(var10, par1World, var6, var7, var8, true)) {
						return false;
					}

					if (Block.blocksList[var10].getMobilityFlag() != 1) {
						if (var9 == 12) {
							return false;
						}

						var6 += Facing.offsetsXForSide[par5];
						var7 += Facing.offsetsYForSide[par5];
						var8 += Facing.offsetsZForSide[par5];
						++var9;
						continue;
					}

					Block.blocksList[var10].dropBlockAsItem(par1World, var6, var7, var8,
							par1World.getBlockMetadata(var6, var7, var8), 0);
					par1World.setBlockToAir(var6, var7, var8);
				}
			}

			var9 = var6;
			var10 = var7;
			int var11 = var8;
			int var12 = 0;
			int[] var13;
			int var14;
			int var15;
			int var16;

			for (var13 = new int[13]; var6 != par2 || var7 != par3 || var8 != par4; var8 = var16) {
				var14 = var6 - Facing.offsetsXForSide[par5];
				var15 = var7 - Facing.offsetsYForSide[par5];
				var16 = var8 - Facing.offsetsZForSide[par5];
				int var17 = par1World.getBlockId(var14, var15, var16);
				int var18 = par1World.getBlockMetadata(var14, var15, var16);

				if (var17 == this.blockID && var14 == par2 && var15 == par3 && var16 == par4) {
					par1World.setBlock(var6, var7, var8, Block.pistonMoving.blockID, par5 | (this.isSticky ? 8 : 0), 4);
					par1World.setBlockTileEntity(var6, var7, var8, BlockPistonMoving.getTileEntity(
							Block.pistonExtension.blockID, par5 | (this.isSticky ? 8 : 0), par5, true, false));
				} else {
					par1World.setBlock(var6, var7, var8, Block.pistonMoving.blockID, var18, 4);
					par1World.setBlockTileEntity(var6, var7, var8,
							BlockPistonMoving.getTileEntity(var17, var18, par5, true, false));
				}

				var13[var12++] = var17;
				var6 = var14;
				var7 = var15;
			}

			var6 = var9;
			var7 = var10;
			var8 = var11;

			for (var12 = 0; var6 != par2 || var7 != par3 || var8 != par4; var8 = var16) {
				var14 = var6 - Facing.offsetsXForSide[par5];
				var15 = var7 - Facing.offsetsYForSide[par5];
				var16 = var8 - Facing.offsetsZForSide[par5];
				par1World.notifyBlocksOfNeighborChange(var14, var15, var16, var13[var12++]);
				var6 = var14;
				var7 = var15;
			}

			return true;
		}
	}
}
