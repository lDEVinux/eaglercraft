package net.minecraft.src;

import java.util.List;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockHopper extends BlockContainer {
	private final EaglercraftRandom field_94457_a = new EaglercraftRandom();
	private Icon hopperIcon;
	private Icon hopperTopIcon;
	private Icon hopperInsideIcon;

	public BlockHopper(int par1) {
		super(par1, Material.iron);
		this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Adds all intersecting collision boxes to a list. (Be sure to only add boxes
	 * to the list if they intersect the mask.) Parameters: World, X, Y, Z, mask,
	 * list, colliding entity
	 */
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		float var8 = 0.125F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, var8, 1.0F, 1.0F);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var8);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		this.setBlockBounds(1.0F - var8, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		this.setBlockBounds(0.0F, 0.0F, 1.0F - var8, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z,
	 * side, hitX, hitY, hitZ, block metadata
	 */
	public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9) {
		int var10 = Facing.oppositeSide[par5];

		if (var10 == 1) {
			var10 = 0;
		}

		return var10;
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the
	 * block.
	 */
	public TileEntity createNewTileEntity(World par1World) {
		return new TileEntityHopper();
	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack) {
		super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLiving, par6ItemStack);

		if (par6ItemStack.hasDisplayName()) {
			TileEntityHopper var7 = getHopperTile(par1World, par2, par3, par4);
			var7.setInventoryName(par6ItemStack.getDisplayName());
		}
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		super.onBlockAdded(par1World, par2, par3, par4);
		this.updateMetadata(par1World, par2, par3, par4);
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		return true;
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * blockID
	 */
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		this.updateMetadata(par1World, par2, par3, par4);
	}

	/**
	 * Updates the Metadata to include if the Hopper gets powered by Redstone or not
	 */
	private void updateMetadata(World par1World, int par2, int par3, int par4) {
		int var5 = par1World.getBlockMetadata(par2, par3, par4);
		int var6 = getDirectionFromMetadata(var5);
		boolean var7 = !par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
		boolean var8 = getIsBlockNotPoweredFromMetadata(var5);

		if (var7 != var8) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, var6 | (var7 ? 0 : 8), 4);
		}
	}

	/**
	 * ejects contained items into the world, and notifies neighbours of an update,
	 * as appropriate
	 */
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		TileEntityHopper var7 = (TileEntityHopper) par1World.getBlockTileEntity(par2, par3, par4);

		if (var7 != null) {
			for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8) {
				ItemStack var9 = var7.getStackInSlot(var8);

				if (var9 != null) {
					float var10 = this.field_94457_a.nextFloat() * 0.8F + 0.1F;
					float var11 = this.field_94457_a.nextFloat() * 0.8F + 0.1F;
					float var12 = this.field_94457_a.nextFloat() * 0.8F + 0.1F;

					while (var9.stackSize > 0) {
						int var13 = this.field_94457_a.nextInt(21) + 10;

						if (var13 > var9.stackSize) {
							var13 = var9.stackSize;
						}

						var9.stackSize -= var13;
						EntityItem var14 = new EntityItem(par1World, (double) ((float) par2 + var10), (double) ((float) par3 + var11), (double) ((float) par4 + var12), new ItemStack(var9.itemID, var13, var9.getItemDamage()));

						if (var9.hasTagCompound()) {
							var14.getEntityItem().setTagCompound((NBTTagCompound) var9.getTagCompound().copy());
						}

						float var15 = 0.05F;
						var14.motionX = (double) ((float) this.field_94457_a.nextGaussian() * var15);
						var14.motionY = (double) ((float) this.field_94457_a.nextGaussian() * var15 + 0.2F);
						var14.motionZ = (double) ((float) this.field_94457_a.nextGaussian() * var15);
						par1World.spawnEntityInWorld(var14);
					}
				}
			}

			par1World.func_96440_m(par2, par3, par4, par5);
		}

		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 38;
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
	 * Returns true if the given side of this block type should be rendered, if the
	 * adjacent block is at the given coordinates. Args: blockAccess, x, y, z, side
	 */
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return true;
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		return par1 == 1 ? this.hopperTopIcon : this.hopperIcon;
	}

	public static int getDirectionFromMetadata(int par0) {
		return par0 & 7;
	}

	public static boolean getIsBlockNotPoweredFromMetadata(int par0) {
		return (par0 & 8) != 8;
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
		return Container.calcRedstoneFromInventory(getHopperTile(par1World, par2, par3, par4));
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.hopperIcon = par1IconRegister.registerIcon("hopper");
		this.hopperTopIcon = par1IconRegister.registerIcon("hopper_top");
		this.hopperInsideIcon = par1IconRegister.registerIcon("hopper_inside");
	}

	public static Icon getHopperIcon(String par0Str) {
		return par0Str == "hopper" ? Block.hopperBlock.hopperIcon : (par0Str == "hopper_inside" ? Block.hopperBlock.hopperInsideIcon : null);
	}

	/**
	 * Gets the icon name of the ItemBlock corresponding to this block. Used by
	 * hoppers.
	 */
	public String getItemIconName() {
		return "hopper";
	}

	public static TileEntityHopper getHopperTile(IBlockAccess par0IBlockAccess, int par1, int par2, int par3) {
		return (TileEntityHopper) par0IBlockAccess.getBlockTileEntity(par1, par2, par3);
	}
}
