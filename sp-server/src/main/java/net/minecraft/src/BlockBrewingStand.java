package net.minecraft.src;

import java.util.List;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockBrewingStand extends BlockContainer {
	private EaglercraftRandom rand = new EaglercraftRandom();

	public BlockBrewingStand(int par1) {
		super(par1, Material.iron);
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
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 25;
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the
	 * block.
	 */
	public TileEntity createNewTileEntity(World par1World) {
		return new TileEntityBrewingStand();
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
	 * Adds all intersecting collision boxes to a list. (Be sure to only add boxes
	 * to the list if they intersect the mask.) Parameters: World, X, Y, Z, mask,
	 * list, colliding entity
	 */
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB,
			List par6List, Entity par7Entity) {
		this.setBlockBounds(0.4375F, 0.0F, 0.4375F, 0.5625F, 0.875F, 0.5625F);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		this.setBlockBoundsForItemRender();
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
	}

	/**
	 * Sets the block's bounds for rendering it as an item
	 */
	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer,
			int par6, float par7, float par8, float par9) {
		if (par1World.isRemote) {
			return true;
		} else {
			TileEntityBrewingStand var10 = (TileEntityBrewingStand) par1World.getBlockTileEntity(par2, par3, par4);

			if (var10 != null) {
				par5EntityPlayer.displayGUIBrewingStand(var10);
			}

			return true;
		}
	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving,
			ItemStack par6ItemStack) {
		if (par6ItemStack.hasDisplayName()) {
			((TileEntityBrewingStand) par1World.getBlockTileEntity(par2, par3, par4))
					.func_94131_a(par6ItemStack.getDisplayName());
		}
	}

	/**
	 * ejects contained items into the world, and notifies neighbours of an update,
	 * as appropriate
	 */
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		TileEntity var7 = par1World.getBlockTileEntity(par2, par3, par4);

		if (var7 instanceof TileEntityBrewingStand) {
			TileEntityBrewingStand var8 = (TileEntityBrewingStand) var7;

			for (int var9 = 0; var9 < var8.getSizeInventory(); ++var9) {
				ItemStack var10 = var8.getStackInSlot(var9);

				if (var10 != null) {
					float var11 = this.rand.nextFloat() * 0.8F + 0.1F;
					float var12 = this.rand.nextFloat() * 0.8F + 0.1F;
					float var13 = this.rand.nextFloat() * 0.8F + 0.1F;

					while (var10.stackSize > 0) {
						int var14 = this.rand.nextInt(21) + 10;

						if (var14 > var10.stackSize) {
							var14 = var10.stackSize;
						}

						var10.stackSize -= var14;
						EntityItem var15 = new EntityItem(par1World, (double) ((float) par2 + var11),
								(double) ((float) par3 + var12), (double) ((float) par4 + var13),
								new ItemStack(var10.itemID, var14, var10.getItemDamage()));
						float var16 = 0.05F;
						var15.motionX = (double) ((float) this.rand.nextGaussian() * var16);
						var15.motionY = (double) ((float) this.rand.nextGaussian() * var16 + 0.2F);
						var15.motionZ = (double) ((float) this.rand.nextGaussian() * var16);
						par1World.spawnEntityInWorld(var15);
					}
				}
			}
		}

		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Item.brewingStand.itemID;
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
