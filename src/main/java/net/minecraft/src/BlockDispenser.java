package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockDispenser extends BlockContainer {
	/** Registry for all dispense behaviors. */
	protected EaglercraftRandom random = new EaglercraftRandom();
	protected Icon furnaceTopIcon;
	protected Icon furnaceFrontIcon;
	protected Icon field_96473_e;

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
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		int var3 = par2 & 7;
		return par1 == var3 ? (var3 != 1 && var3 != 0 ? this.furnaceFrontIcon : this.field_96473_e) : (var3 != 1 && var3 != 0 ? (par1 != 1 && par1 != 0 ? this.blockIcon : this.furnaceTopIcon) : this.furnaceTopIcon);
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("furnace_side");
		this.furnaceTopIcon = par1IconRegister.registerIcon("furnace_top");
		this.furnaceFrontIcon = par1IconRegister.registerIcon("dispenser_front");
		this.field_96473_e = par1IconRegister.registerIcon("dispenser_front_vertical");
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		return true;
	}

	protected void dispense(World par1World, int par2, int par3, int par4) {
		
	}

	/**
	 * Returns the behavior for the given ItemStack.
	 */
	protected IBehaviorDispenseItem getBehaviorForItemStack(ItemStack par1ItemStack) {
		return null;
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * blockID
	 */
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		boolean var6 = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4) || par1World.isBlockIndirectlyGettingPowered(par2, par3 + 1, par4);
		int var7 = par1World.getBlockMetadata(par2, par3, par4);
		boolean var8 = (var7 & 8) != 0;

		if (var6 && !var8) {
			par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
			par1World.setBlockMetadataWithNotify(par2, par3, par4, var7 | 8, 4);
		} else if (!var6 && var8) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, var7 & -9, 4);
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
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack) {
		int var7 = BlockPistonBase.determineOrientation(par1World, par2, par3, par4, par5EntityLiving);
		par1World.setBlockMetadataWithNotify(par2, par3, par4, var7, 2);

		if (par6ItemStack.hasDisplayName()) {
			((TileEntityDispenser) par1World.getBlockTileEntity(par2, par3, par4)).setCustomName(par6ItemStack.getDisplayName());
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
						EntityItem var14 = new EntityItem(par1World, (double) ((float) par2 + var10), (double) ((float) par3 + var11), (double) ((float) par4 + var12), new ItemStack(var9.itemID, var13, var9.getItemDamage()));

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
