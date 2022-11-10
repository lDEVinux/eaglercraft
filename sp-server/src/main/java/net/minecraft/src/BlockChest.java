package net.minecraft.src;

import java.util.Iterator;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockChest extends BlockContainer {
	private final EaglercraftRandom random = new EaglercraftRandom();

	/** Determines whether of not the chest is trapped. */
	public final int isTrapped;

	protected BlockChest(int par1, int par2) {
		super(par1, Material.wood);
		this.isTrapped = par2;
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
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		if (par1IBlockAccess.getBlockId(par2, par3, par4 - 1) == this.blockID) {
			this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
		} else if (par1IBlockAccess.getBlockId(par2, par3, par4 + 1) == this.blockID) {
			this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
		} else if (par1IBlockAccess.getBlockId(par2 - 1, par3, par4) == this.blockID) {
			this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
		} else if (par1IBlockAccess.getBlockId(par2 + 1, par3, par4) == this.blockID) {
			this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
		} else {
			this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
		}
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		super.onBlockAdded(par1World, par2, par3, par4);
		this.unifyAdjacentChests(par1World, par2, par3, par4);
		int var5 = par1World.getBlockId(par2, par3, par4 - 1);
		int var6 = par1World.getBlockId(par2, par3, par4 + 1);
		int var7 = par1World.getBlockId(par2 - 1, par3, par4);
		int var8 = par1World.getBlockId(par2 + 1, par3, par4);

		if (var5 == this.blockID) {
			this.unifyAdjacentChests(par1World, par2, par3, par4 - 1);
		}

		if (var6 == this.blockID) {
			this.unifyAdjacentChests(par1World, par2, par3, par4 + 1);
		}

		if (var7 == this.blockID) {
			this.unifyAdjacentChests(par1World, par2 - 1, par3, par4);
		}

		if (var8 == this.blockID) {
			this.unifyAdjacentChests(par1World, par2 + 1, par3, par4);
		}
	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving,
			ItemStack par6ItemStack) {
		int var7 = par1World.getBlockId(par2, par3, par4 - 1);
		int var8 = par1World.getBlockId(par2, par3, par4 + 1);
		int var9 = par1World.getBlockId(par2 - 1, par3, par4);
		int var10 = par1World.getBlockId(par2 + 1, par3, par4);
		byte var11 = 0;
		int var12 = MathHelper.floor_double((double) (par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (var12 == 0) {
			var11 = 2;
		}

		if (var12 == 1) {
			var11 = 5;
		}

		if (var12 == 2) {
			var11 = 3;
		}

		if (var12 == 3) {
			var11 = 4;
		}

		if (var7 != this.blockID && var8 != this.blockID && var9 != this.blockID && var10 != this.blockID) {
			par1World.setBlockMetadata(par2, par3, par4, var11, 3);
		} else {
			if ((var7 == this.blockID || var8 == this.blockID) && (var11 == 4 || var11 == 5)) {
				if (var7 == this.blockID) {
					par1World.setBlockMetadata(par2, par3, par4 - 1, var11, 3);
				} else {
					par1World.setBlockMetadata(par2, par3, par4 + 1, var11, 3);
				}

				par1World.setBlockMetadata(par2, par3, par4, var11, 3);
			}

			if ((var9 == this.blockID || var10 == this.blockID) && (var11 == 2 || var11 == 3)) {
				if (var9 == this.blockID) {
					par1World.setBlockMetadata(par2 - 1, par3, par4, var11, 3);
				} else {
					par1World.setBlockMetadata(par2 + 1, par3, par4, var11, 3);
				}

				par1World.setBlockMetadata(par2, par3, par4, var11, 3);
			}
		}

		if (par6ItemStack.hasDisplayName()) {
			((TileEntityChest) par1World.getBlockTileEntity(par2, par3, par4))
					.func_94043_a(par6ItemStack.getDisplayName());
		}
	}

	/**
	 * Turns the adjacent chests to a double chest.
	 */
	public void unifyAdjacentChests(World par1World, int par2, int par3, int par4) {
		if (!par1World.isRemote) {
			int var5 = par1World.getBlockId(par2, par3, par4 - 1);
			int var6 = par1World.getBlockId(par2, par3, par4 + 1);
			int var7 = par1World.getBlockId(par2 - 1, par3, par4);
			int var8 = par1World.getBlockId(par2 + 1, par3, par4);
			boolean var9 = true;
			int var10;
			int var11;
			boolean var12;
			byte var13;
			int var14;

			if (var5 != this.blockID && var6 != this.blockID) {
				if (var7 != this.blockID && var8 != this.blockID) {
					var13 = 3;

					if (Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var6]) {
						var13 = 3;
					}

					if (Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var5]) {
						var13 = 2;
					}

					if (Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var8]) {
						var13 = 5;
					}

					if (Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var7]) {
						var13 = 4;
					}
				} else {
					var10 = par1World.getBlockId(var7 == this.blockID ? par2 - 1 : par2 + 1, par3, par4 - 1);
					var11 = par1World.getBlockId(var7 == this.blockID ? par2 - 1 : par2 + 1, par3, par4 + 1);
					var13 = 3;
					var12 = true;

					if (var7 == this.blockID) {
						var14 = par1World.getBlockMetadata(par2 - 1, par3, par4);
					} else {
						var14 = par1World.getBlockMetadata(par2 + 1, par3, par4);
					}

					if (var14 == 2) {
						var13 = 2;
					}

					if ((Block.opaqueCubeLookup[var5] || Block.opaqueCubeLookup[var10]) && !Block.opaqueCubeLookup[var6]
							&& !Block.opaqueCubeLookup[var11]) {
						var13 = 3;
					}

					if ((Block.opaqueCubeLookup[var6] || Block.opaqueCubeLookup[var11]) && !Block.opaqueCubeLookup[var5]
							&& !Block.opaqueCubeLookup[var10]) {
						var13 = 2;
					}
				}
			} else {
				var10 = par1World.getBlockId(par2 - 1, par3, var5 == this.blockID ? par4 - 1 : par4 + 1);
				var11 = par1World.getBlockId(par2 + 1, par3, var5 == this.blockID ? par4 - 1 : par4 + 1);
				var13 = 5;
				var12 = true;

				if (var5 == this.blockID) {
					var14 = par1World.getBlockMetadata(par2, par3, par4 - 1);
				} else {
					var14 = par1World.getBlockMetadata(par2, par3, par4 + 1);
				}

				if (var14 == 4) {
					var13 = 4;
				}

				if ((Block.opaqueCubeLookup[var7] || Block.opaqueCubeLookup[var10]) && !Block.opaqueCubeLookup[var8]
						&& !Block.opaqueCubeLookup[var11]) {
					var13 = 5;
				}

				if ((Block.opaqueCubeLookup[var8] || Block.opaqueCubeLookup[var11]) && !Block.opaqueCubeLookup[var7]
						&& !Block.opaqueCubeLookup[var10]) {
					var13 = 4;
				}
			}

			par1World.setBlockMetadata(par2, par3, par4, var13, 3);
		}
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates.
	 * Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		int var5 = 0;

		if (par1World.getBlockId(par2 - 1, par3, par4) == this.blockID) {
			++var5;
		}

		if (par1World.getBlockId(par2 + 1, par3, par4) == this.blockID) {
			++var5;
		}

		if (par1World.getBlockId(par2, par3, par4 - 1) == this.blockID) {
			++var5;
		}

		if (par1World.getBlockId(par2, par3, par4 + 1) == this.blockID) {
			++var5;
		}

		return var5 > 1 ? false
				: (this.isThereANeighborChest(par1World, par2 - 1, par3, par4) ? false
						: (this.isThereANeighborChest(par1World, par2 + 1, par3, par4) ? false
								: (this.isThereANeighborChest(par1World, par2, par3, par4 - 1) ? false
										: !this.isThereANeighborChest(par1World, par2, par3, par4 + 1))));
	}

	/**
	 * Checks the neighbor blocks to see if there is a chest there. Args: world, x,
	 * y, z
	 */
	private boolean isThereANeighborChest(World par1World, int par2, int par3, int par4) {
		return par1World.getBlockId(par2, par3, par4) != this.blockID ? false
				: (par1World.getBlockId(par2 - 1, par3, par4) == this.blockID ? true
						: (par1World.getBlockId(par2 + 1, par3, par4) == this.blockID ? true
								: (par1World.getBlockId(par2, par3, par4 - 1) == this.blockID ? true
										: par1World.getBlockId(par2, par3, par4 + 1) == this.blockID)));
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * blockID
	 */
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
		TileEntityChest var6 = (TileEntityChest) par1World.getBlockTileEntity(par2, par3, par4);

		if (var6 != null) {
			var6.updateContainingBlockInfo();
		}
	}

	/**
	 * ejects contained items into the world, and notifies neighbours of an update,
	 * as appropriate
	 */
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		TileEntityChest var7 = (TileEntityChest) par1World.getBlockTileEntity(par2, par3, par4);

		if (var7 != null) {
			for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8) {
				ItemStack var9 = var7.getStackInSlot(var8);

				if (var9 != null) {
					float var10 = this.random.nextFloat() * 0.8F + 0.1F;
					float var11 = this.random.nextFloat() * 0.8F + 0.1F;
					EntityItem var14;

					for (float var12 = this.random.nextFloat() * 0.8F + 0.1F; var9.stackSize > 0; par1World
							.spawnEntityInWorld(var14)) {
						int var13 = this.random.nextInt(21) + 10;

						if (var13 > var9.stackSize) {
							var13 = var9.stackSize;
						}

						var9.stackSize -= var13;
						var14 = new EntityItem(par1World, (double) ((float) par2 + var10),
								(double) ((float) par3 + var11), (double) ((float) par4 + var12),
								new ItemStack(var9.itemID, var13, var9.getItemDamage()));
						float var15 = 0.05F;
						var14.motionX = (double) ((float) this.random.nextGaussian() * var15);
						var14.motionY = (double) ((float) this.random.nextGaussian() * var15 + 0.2F);
						var14.motionZ = (double) ((float) this.random.nextGaussian() * var15);

						if (var9.hasTagCompound()) {
							var14.getEntityItem().setTagCompound((NBTTagCompound) var9.getTagCompound().copy());
						}
					}
				}
			}

			par1World.func_96440_m(par2, par3, par4, par5);
		}

		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer,
			int par6, float par7, float par8, float par9) {
		if (par1World.isRemote) {
			return true;
		} else {
			IInventory var10 = this.getInventory(par1World, par2, par3, par4);

			if (var10 != null) {
				par5EntityPlayer.displayGUIChest(var10);
			}

			return true;
		}
	}

	/**
	 * Gets the inventory of the chest at the specified coords, accounting for
	 * blocks or ocelots on top of the chest, and double chests.
	 */
	public IInventory getInventory(World par1World, int par2, int par3, int par4) {
		Object var5 = (TileEntityChest) par1World.getBlockTileEntity(par2, par3, par4);

		if (var5 == null) {
			return null;
		} else if (par1World.isBlockNormalCube(par2, par3 + 1, par4)) {
			return null;
		} else if (isOcelotBlockingChest(par1World, par2, par3, par4)) {
			return null;
		} else if (par1World.getBlockId(par2 - 1, par3, par4) == this.blockID
				&& (par1World.isBlockNormalCube(par2 - 1, par3 + 1, par4)
						|| isOcelotBlockingChest(par1World, par2 - 1, par3, par4))) {
			return null;
		} else if (par1World.getBlockId(par2 + 1, par3, par4) == this.blockID
				&& (par1World.isBlockNormalCube(par2 + 1, par3 + 1, par4)
						|| isOcelotBlockingChest(par1World, par2 + 1, par3, par4))) {
			return null;
		} else if (par1World.getBlockId(par2, par3, par4 - 1) == this.blockID
				&& (par1World.isBlockNormalCube(par2, par3 + 1, par4 - 1)
						|| isOcelotBlockingChest(par1World, par2, par3, par4 - 1))) {
			return null;
		} else if (par1World.getBlockId(par2, par3, par4 + 1) == this.blockID
				&& (par1World.isBlockNormalCube(par2, par3 + 1, par4 + 1)
						|| isOcelotBlockingChest(par1World, par2, par3, par4 + 1))) {
			return null;
		} else {
			if (par1World.getBlockId(par2 - 1, par3, par4) == this.blockID) {
				var5 = new InventoryLargeChest("container.chestDouble",
						(TileEntityChest) par1World.getBlockTileEntity(par2 - 1, par3, par4), (IInventory) var5);
			}

			if (par1World.getBlockId(par2 + 1, par3, par4) == this.blockID) {
				var5 = new InventoryLargeChest("container.chestDouble", (IInventory) var5,
						(TileEntityChest) par1World.getBlockTileEntity(par2 + 1, par3, par4));
			}

			if (par1World.getBlockId(par2, par3, par4 - 1) == this.blockID) {
				var5 = new InventoryLargeChest("container.chestDouble",
						(TileEntityChest) par1World.getBlockTileEntity(par2, par3, par4 - 1), (IInventory) var5);
			}

			if (par1World.getBlockId(par2, par3, par4 + 1) == this.blockID) {
				var5 = new InventoryLargeChest("container.chestDouble", (IInventory) var5,
						(TileEntityChest) par1World.getBlockTileEntity(par2, par3, par4 + 1));
			}

			return (IInventory) var5;
		}
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the
	 * block.
	 */
	public TileEntity createNewTileEntity(World par1World) {
		TileEntityChest var2 = new TileEntityChest();
		return var2;
	}

	/**
	 * Can this block provide power. Only wire currently seems to have this change
	 * based on its state.
	 */
	public boolean canProvidePower() {
		return this.isTrapped == 1;
	}

	/**
	 * Returns true if the block is emitting indirect/weak redstone power on the
	 * specified side. If isBlockNormalCube returns true, standard redstone
	 * propagation rules will apply instead and this will not be called. Args:
	 * World, X, Y, Z, side. Note that the side is reversed - eg it is 1 (up) when
	 * checking the bottom of the block.
	 */
	public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		if (!this.canProvidePower()) {
			return 0;
		} else {
			int var6 = ((TileEntityChest) par1IBlockAccess.getBlockTileEntity(par2, par3, par4)).numUsingPlayers;
			return MathHelper.clamp_int(var6, 0, 15);
		}
	}

	/**
	 * Returns true if the block is emitting direct/strong redstone power on the
	 * specified side. Args: World, X, Y, Z, side. Note that the side is reversed -
	 * eg it is 1 (up) when checking the bottom of the block.
	 */
	public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return par5 == 1 ? this.isProvidingWeakPower(par1IBlockAccess, par2, par3, par4, par5) : 0;
	}

	/**
	 * Looks for a sitting ocelot within certain bounds. Such an ocelot is
	 * considered to be blocking access to the chest.
	 */
	private static boolean isOcelotBlockingChest(World par0World, int par1, int par2, int par3) {
		Iterator var4 = par0World.getEntitiesWithinAABB(EntityOcelot.class,
				AxisAlignedBB.getAABBPool().getAABB((double) par1, (double) (par2 + 1), (double) par3,
						(double) (par1 + 1), (double) (par2 + 2), (double) (par3 + 1)))
				.iterator();
		EntityOcelot var6;

		do {
			if (!var4.hasNext()) {
				return false;
			}

			EntityOcelot var5 = (EntityOcelot) var4.next();
			var6 = (EntityOcelot) var5;
		} while (!var6.isSitting());

		return true;
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
		return Container.calcRedstoneFromInventory(this.getInventory(par1World, par2, par3, par4));
	}
}
