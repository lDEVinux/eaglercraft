package net.minecraft.src;

public abstract class BlockBasePressurePlate extends Block {
	private String pressurePlateIconName;

	protected BlockBasePressurePlate(int par1, String par2Str, Material par3Material) {
		super(par1, par3Material);
		this.pressurePlateIconName = par2Str;
		this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setTickRandomly(true);
		this.func_94353_c_(this.getMetaFromWeight(15));
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		this.func_94353_c_(par1IBlockAccess.getBlockMetadata(par2, par3, par4));
	}

	protected void func_94353_c_(int par1) {
		boolean var2 = this.getPowerSupply(par1) > 0;
		float var3 = 0.0625F;

		if (var2) {
			this.setBlockBounds(var3, 0.0F, var3, 1.0F - var3, 0.03125F, 1.0F - var3);
		} else {
			this.setBlockBounds(var3, 0.0F, var3, 1.0F - var3, 0.0625F, 1.0F - var3);
		}
	}

	/**
	 * How many world ticks before ticking
	 */
	public int tickRate(World par1World) {
		return 20;
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

	public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		return true;
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates.
	 * Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		return par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) || BlockFence.isIdAFence(par1World.getBlockId(par2, par3 - 1, par4));
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * blockID
	 */
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		boolean var6 = false;

		if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && !BlockFence.isIdAFence(par1World.getBlockId(par2, par3 - 1, par4))) {
			var6 = true;
		}

		if (var6) {
			this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
			par1World.setBlockToAir(par2, par3, par4);
		}
	}

	/**
	 * Checks if there are mobs on the plate. If a mob is on the plate and it is
	 * off, it turns it on, and vice versa.
	 */
	protected void setStateIfMobInteractsWithPlate(World par1World, int par2, int par3, int par4, int par5) {
		int var6 = this.getPlateState(par1World, par2, par3, par4);
		boolean var7 = par5 > 0;
		boolean var8 = var6 > 0;

		if (par5 != var6) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, this.getMetaFromWeight(var6), 2);
			this.func_94354_b_(par1World, par2, par3, par4);
			par1World.markBlockRangeForRenderUpdate(par2, par3, par4, par2, par3, par4);
		}

		if (!var8 && var7) {
			par1World.playSoundEffect((double) par2 + 0.5D, (double) par3 + 0.1D, (double) par4 + 0.5D, "random.click", 0.3F, 0.5F);
		} else if (var8 && !var7) {
			par1World.playSoundEffect((double) par2 + 0.5D, (double) par3 + 0.1D, (double) par4 + 0.5D, "random.click", 0.3F, 0.6F);
		}

		if (var8) {
			par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
		}
	}

	protected AxisAlignedBB getSensitiveAABB(int par1, int par2, int par3) {
		float var4 = 0.125F;
		return AxisAlignedBB.getAABBPool().getAABB((double) ((float) par1 + var4), (double) par2, (double) ((float) par3 + var4), (double) ((float) (par1 + 1) - var4), (double) par2 + 0.25D, (double) ((float) (par3 + 1) - var4));
	}

	/**
	 * ejects contained items into the world, and notifies neighbours of an update,
	 * as appropriate
	 */
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		if (this.getPowerSupply(par6) > 0) {
			this.func_94354_b_(par1World, par2, par3, par4);
		}

		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	protected void func_94354_b_(World par1World, int par2, int par3, int par4) {
		par1World.notifyBlocksOfNeighborChange(par2, par3, par4, this.blockID);
		par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this.blockID);
	}

	/**
	 * Returns true if the block is emitting indirect/weak redstone power on the
	 * specified side. If isBlockNormalCube returns true, standard redstone
	 * propagation rules will apply instead and this will not be called. Args:
	 * World, X, Y, Z, side. Note that the side is reversed - eg it is 1 (up) when
	 * checking the bottom of the block.
	 */
	public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return this.getPowerSupply(par1IBlockAccess.getBlockMetadata(par2, par3, par4));
	}

	/**
	 * Returns true if the block is emitting direct/strong redstone power on the
	 * specified side. Args: World, X, Y, Z, side. Note that the side is reversed -
	 * eg it is 1 (up) when checking the bottom of the block.
	 */
	public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return par5 == 1 ? this.getPowerSupply(par1IBlockAccess.getBlockMetadata(par2, par3, par4)) : 0;
	}

	/**
	 * Can this block provide power. Only wire currently seems to have this change
	 * based on its state.
	 */
	public boolean canProvidePower() {
		return true;
	}

	/**
	 * Sets the block's bounds for rendering it as an item
	 */
	public void setBlockBoundsForItemRender() {
		float var1 = 0.5F;
		float var2 = 0.125F;
		float var3 = 0.5F;
		this.setBlockBounds(0.5F - var1, 0.5F - var2, 0.5F - var3, 0.5F + var1, 0.5F + var2, 0.5F + var3);
	}

	/**
	 * Returns the mobility information of the block, 0 = free, 1 = can't push but
	 * can move over, 2 = total immobility and stop pistons
	 */
	public int getMobilityFlag() {
		return 1;
	}

	/**
	 * Returns the current state of the pressure plate. Returns a value between 0
	 * and 15 based on the number of items on it.
	 */
	protected abstract int getPlateState(World var1, int var2, int var3, int var4);

	/**
	 * Argument is metadata. Returns power level (0-15)
	 */
	protected abstract int getPowerSupply(int var1);

	/**
	 * Argument is weight (0-15). Return the metadata to be set because of it.
	 */
	protected abstract int getMetaFromWeight(int var1);

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon(this.pressurePlateIconName);
	}
}
