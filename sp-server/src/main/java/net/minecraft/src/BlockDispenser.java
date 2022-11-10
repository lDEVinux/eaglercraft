package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockDispenser extends BlockContainer {
	/** Registry for all dispense behaviors. */
	public static final IRegistry dispenseBehaviorRegistry = new RegistryDefaulted(new BehaviorDefaultDispenseItem());
	protected EaglercraftRandom random = new EaglercraftRandom();

	protected BlockDispenser(int par1) {
		super(par1, Material.rock);
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	/**
	 * How many world ticks before ticking
	 */
	public int tickRate(World par1World) {
		return 4;
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		super.onBlockAdded(par1World, par2, par3, par4);
		this.setDispenserDefaultDirection(par1World, par2, par3, par4);
	}

	/**
	 * sets Dispenser block direction so that the front faces an non-opaque block;
	 * chooses west to be direction if all surrounding blocks are opaque.
	 */
	private void setDispenserDefaultDirection(World par1World, int par2, int par3, int par4) {
		if (!par1World.isRemote) {
			int var5 = par1World.getBlockId(par2, par3, par4 - 1);
			int var6 = par1World.getBlockId(par2, par3, par4 + 1);
			int var7 = par1World.getBlockId(par2 - 1, par3, par4);
			int var8 = par1World.getBlockId(par2 + 1, par3, par4);
			byte var9 = 3;

			if (Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var6]) {
				var9 = 3;
			}

			if (Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var5]) {
				var9 = 2;
			}

			if (Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var8]) {
				var9 = 5;
			}

			if (Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var7]) {
				var9 = 4;
			}

			par1World.setBlockMetadata(par2, par3, par4, var9, 2);
		}
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer,
			int par6, float par7, float par8, float par9) {
		if (par1World.isRemote) {
			return true;
		} else {
			TileEntityDispenser var10 = (TileEntityDispenser) par1World.getBlockTileEntity(par2, par3, par4);

			if (var10 != null) {
				par5EntityPlayer.displayGUIDispenser(var10);
			}

			return true;
		}
	}

	protected void dispense(World par1World, int par2, int par3, int par4) {
		BlockSourceImpl var5 = new BlockSourceImpl(par1World, par2, par3, par4);
		TileEntityDispenser var6 = (TileEntityDispenser) var5.getBlockTileEntity();

		if (var6 != null) {
			int var7 = var6.getRandomStackFromInventory();

			if (var7 < 0) {
				par1World.playAuxSFX(1001, par2, par3, par4, 0);
			} else {
				ItemStack var8 = var6.getStackInSlot(var7);
				IBehaviorDispenseItem var9 = this.getBehaviorForItemStack(var8);

				if (var9 != IBehaviorDispenseItem.itemDispenseBehaviorProvider) {
					ItemStack var10 = var9.dispense(var5, var8);
					var6.setInventorySlotContents(var7, var10.stackSize == 0 ? null : var10);
				}
			}
		}
	}

	/**
	 * Returns the behavior for the given ItemStack.
	 */
	protected IBehaviorDispenseItem getBehaviorForItemStack(ItemStack par1ItemStack) {
		return (IBehaviorDispenseItem) dispenseBehaviorRegistry.func_82594_a(par1ItemStack.getItem());
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * blockID
	 */
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		boolean var6 = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4)
				|| par1World.isBlockIndirectlyGettingPowered(par2, par3 + 1, par4);
		int var7 = par1World.getBlockMetadata(par2, par3, par4);
		boolean var8 = (var7 & 8) != 0;

		if (var6 && !var8) {
			par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
			par1World.setBlockMetadata(par2, par3, par4, var7 | 8, 4);
		} else if (!var6 && var8) {
			par1World.setBlockMetadata(par2, par3, par4, var7 & -9, 4);
		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		if (!par1World.isRemote) {
			this.dispense(par1World, par2, par3, par4);
		}
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the
	 * block.
	 */
	public TileEntity createNewTileEntity(World par1World) {
		return new TileEntityDispenser();
	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving,
			ItemStack par6ItemStack) {
		int var7 = BlockPistonBase.determineOrientation(par1World, par2, par3, par4, par5EntityLiving);
		par1World.setBlockMetadata(par2, par3, par4, var7, 2);

		if (par6ItemStack.hasDisplayName()) {
			((TileEntityDispenser) par1World.getBlockTileEntity(par2, par3, par4))
					.func_94049_a(par6ItemStack.getDisplayName());
		}
	}

	/**
	 * ejects contained items into the world, and notifies neighbours of an update,
	 * as appropriate
	 */
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		TileEntityDispenser var7 = (TileEntityDispenser) par1World.getBlockTileEntity(par2, par3, par4);

		if (var7 != null) {
			for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8) {
				ItemStack var9 = var7.getStackInSlot(var8);

				if (var9 != null) {
					float var10 = this.random.nextFloat() * 0.8F + 0.1F;
					float var11 = this.random.nextFloat() * 0.8F + 0.1F;
					float var12 = this.random.nextFloat() * 0.8F + 0.1F;

					while (var9.stackSize > 0) {
						int var13 = this.random.nextInt(21) + 10;

						if (var13 > var9.stackSize) {
							var13 = var9.stackSize;
						}

						var9.stackSize -= var13;
						EntityItem var14 = new EntityItem(par1World, (double) ((float) par2 + var10),
								(double) ((float) par3 + var11), (double) ((float) par4 + var12),
								new ItemStack(var9.itemID, var13, var9.getItemDamage()));

						if (var9.hasTagCompound()) {
							var14.getEntityItem().setTagCompound((NBTTagCompound) var9.getTagCompound().copy());
						}

						float var15 = 0.05F;
						var14.motionX = (double) ((float) this.random.nextGaussian() * var15);
						var14.motionY = (double) ((float) this.random.nextGaussian() * var15 + 0.2F);
						var14.motionZ = (double) ((float) this.random.nextGaussian() * var15);
						par1World.spawnEntityInWorld(var14);
					}
				}
			}

			par1World.func_96440_m(par2, par3, par4, par5);
		}

		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	public static IPosition getIPositionFromBlockSource(IBlockSource par0IBlockSource) {
		EnumFacing var1 = getFacing(par0IBlockSource.getBlockMetadata());
		double var2 = par0IBlockSource.getX() + 0.7D * (double) var1.getFrontOffsetX();
		double var4 = par0IBlockSource.getY() + 0.7D * (double) var1.getFrontOffsetY();
		double var6 = par0IBlockSource.getZ() + 0.7D * (double) var1.getFrontOffsetZ();
		return new PositionImpl(var2, var4, var6);
	}

	public static EnumFacing getFacing(int par0) {
		return EnumFacing.getFront(par0 & 7);
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
		return Container.calcRedstoneFromInventory((IInventory) par1World.getBlockTileEntity(par2, par3, par4));
	}
}
