package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockSkull extends BlockContainer {
	protected BlockSkull(int par1) {
		super(par1, Material.circuits);
		this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return -1;
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
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		int var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 7;

		switch (var5) {
		case 1:
		default:
			this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
			break;

		case 2:
			this.setBlockBounds(0.25F, 0.25F, 0.5F, 0.75F, 0.75F, 1.0F);
			break;

		case 3:
			this.setBlockBounds(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.5F);
			break;

		case 4:
			this.setBlockBounds(0.5F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
			break;

		case 5:
			this.setBlockBounds(0.0F, 0.25F, 0.25F, 0.5F, 0.75F, 0.75F);
		}
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
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving,
			ItemStack par6ItemStack) {
		int var7 = MathHelper.floor_double((double) (par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		par1World.setBlockMetadata(par2, par3, par4, var7, 2);
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the
	 * block.
	 */
	public TileEntity createNewTileEntity(World par1World) {
		return new TileEntitySkull();
	}

	/**
	 * Get the block's damage value (for use with pick block).
	 */
	public int getDamageValue(World par1World, int par2, int par3, int par4) {
		TileEntity var5 = par1World.getBlockTileEntity(par2, par3, par4);
		return var5 != null && var5 instanceof TileEntitySkull ? ((TileEntitySkull) var5).getSkullType()
				: super.getDamageValue(par1World, par2, par3, par4);
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and wood.
	 */
	public int damageDropped(int par1) {
		return par1;
	}

	/**
	 * Drops the block items with a specified chance of dropping the specified items
	 */
	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6,
			int par7) {
	}

	/**
	 * Called when the block is attempted to be harvested
	 */
	public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5,
			EntityPlayer par6EntityPlayer) {
		if (par6EntityPlayer.capabilities.isCreativeMode) {
			par5 |= 8;
			par1World.setBlockMetadata(par2, par3, par4, par5, 4);
		}

		super.onBlockHarvested(par1World, par2, par3, par4, par5, par6EntityPlayer);
	}

	/**
	 * ejects contained items into the world, and notifies neighbours of an update,
	 * as appropriate
	 */
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		if (!par1World.isRemote) {
			if ((par6 & 8) == 0) {
				ItemStack var7 = new ItemStack(Item.skull.itemID, 1, this.getDamageValue(par1World, par2, par3, par4));
				TileEntitySkull var8 = (TileEntitySkull) par1World.getBlockTileEntity(par2, par3, par4);

				if (var8.getSkullType() == 3 && var8.getExtraType() != null && var8.getExtraType().length() > 0) {
					var7.setTagCompound(new NBTTagCompound());
					var7.getTagCompound().setString("SkullOwner", var8.getExtraType());
				}

				this.dropBlockAsItem_do(par1World, par2, par3, par4, var7);
			}

			super.breakBlock(par1World, par2, par3, par4, par5, par6);
		}
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Item.skull.itemID;
	}

	/**
	 * This method attempts to create a wither at the given location and skull
	 */
	public void makeWither(World par1World, int par2, int par3, int par4, TileEntitySkull par5TileEntitySkull) {
		if (par5TileEntitySkull.getSkullType() == 1 && par3 >= 2 && par1World.difficultySetting > 0
				&& !par1World.isRemote) {
			int var6 = Block.slowSand.blockID;
			int var7;
			EntityWither var8;
			int var9;

			for (var7 = -2; var7 <= 0; ++var7) {
				if (par1World.getBlockId(par2, par3 - 1, par4 + var7) == var6
						&& par1World.getBlockId(par2, par3 - 1, par4 + var7 + 1) == var6
						&& par1World.getBlockId(par2, par3 - 2, par4 + var7 + 1) == var6
						&& par1World.getBlockId(par2, par3 - 1, par4 + var7 + 2) == var6
						&& this.func_82528_d(par1World, par2, par3, par4 + var7, 1)
						&& this.func_82528_d(par1World, par2, par3, par4 + var7 + 1, 1)
						&& this.func_82528_d(par1World, par2, par3, par4 + var7 + 2, 1)) {
					par1World.setBlockMetadata(par2, par3, par4 + var7, 8, 2);
					par1World.setBlockMetadata(par2, par3, par4 + var7 + 1, 8, 2);
					par1World.setBlockMetadata(par2, par3, par4 + var7 + 2, 8, 2);
					par1World.setBlock(par2, par3, par4 + var7, 0, 0, 2);
					par1World.setBlock(par2, par3, par4 + var7 + 1, 0, 0, 2);
					par1World.setBlock(par2, par3, par4 + var7 + 2, 0, 0, 2);
					par1World.setBlock(par2, par3 - 1, par4 + var7, 0, 0, 2);
					par1World.setBlock(par2, par3 - 1, par4 + var7 + 1, 0, 0, 2);
					par1World.setBlock(par2, par3 - 1, par4 + var7 + 2, 0, 0, 2);
					par1World.setBlock(par2, par3 - 2, par4 + var7 + 1, 0, 0, 2);

					if (!par1World.isRemote) {
						var8 = new EntityWither(par1World);
						var8.setLocationAndAngles((double) par2 + 0.5D, (double) par3 - 1.45D,
								(double) (par4 + var7) + 1.5D, 90.0F, 0.0F);
						var8.renderYawOffset = 90.0F;
						var8.func_82206_m();
						par1World.spawnEntityInWorld(var8);
					}

					for (var9 = 0; var9 < 120; ++var9) {
						par1World.spawnParticle("snowballpoof", (double) par2 + par1World.rand.nextDouble(),
								(double) (par3 - 2) + par1World.rand.nextDouble() * 3.9D,
								(double) (par4 + var7 + 1) + par1World.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
					}

					par1World.notifyBlockChange(par2, par3, par4 + var7, 0);
					par1World.notifyBlockChange(par2, par3, par4 + var7 + 1, 0);
					par1World.notifyBlockChange(par2, par3, par4 + var7 + 2, 0);
					par1World.notifyBlockChange(par2, par3 - 1, par4 + var7, 0);
					par1World.notifyBlockChange(par2, par3 - 1, par4 + var7 + 1, 0);
					par1World.notifyBlockChange(par2, par3 - 1, par4 + var7 + 2, 0);
					par1World.notifyBlockChange(par2, par3 - 2, par4 + var7 + 1, 0);
					return;
				}
			}

			for (var7 = -2; var7 <= 0; ++var7) {
				if (par1World.getBlockId(par2 + var7, par3 - 1, par4) == var6
						&& par1World.getBlockId(par2 + var7 + 1, par3 - 1, par4) == var6
						&& par1World.getBlockId(par2 + var7 + 1, par3 - 2, par4) == var6
						&& par1World.getBlockId(par2 + var7 + 2, par3 - 1, par4) == var6
						&& this.func_82528_d(par1World, par2 + var7, par3, par4, 1)
						&& this.func_82528_d(par1World, par2 + var7 + 1, par3, par4, 1)
						&& this.func_82528_d(par1World, par2 + var7 + 2, par3, par4, 1)) {
					par1World.setBlockMetadata(par2 + var7, par3, par4, 8, 2);
					par1World.setBlockMetadata(par2 + var7 + 1, par3, par4, 8, 2);
					par1World.setBlockMetadata(par2 + var7 + 2, par3, par4, 8, 2);
					par1World.setBlock(par2 + var7, par3, par4, 0, 0, 2);
					par1World.setBlock(par2 + var7 + 1, par3, par4, 0, 0, 2);
					par1World.setBlock(par2 + var7 + 2, par3, par4, 0, 0, 2);
					par1World.setBlock(par2 + var7, par3 - 1, par4, 0, 0, 2);
					par1World.setBlock(par2 + var7 + 1, par3 - 1, par4, 0, 0, 2);
					par1World.setBlock(par2 + var7 + 2, par3 - 1, par4, 0, 0, 2);
					par1World.setBlock(par2 + var7 + 1, par3 - 2, par4, 0, 0, 2);

					if (!par1World.isRemote) {
						var8 = new EntityWither(par1World);
						var8.setLocationAndAngles((double) (par2 + var7) + 1.5D, (double) par3 - 1.45D,
								(double) par4 + 0.5D, 0.0F, 0.0F);
						var8.func_82206_m();
						par1World.spawnEntityInWorld(var8);
					}

					for (var9 = 0; var9 < 120; ++var9) {
						par1World.spawnParticle("snowballpoof",
								(double) (par2 + var7 + 1) + par1World.rand.nextDouble(),
								(double) (par3 - 2) + par1World.rand.nextDouble() * 3.9D,
								(double) par4 + par1World.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
					}

					par1World.notifyBlockChange(par2 + var7, par3, par4, 0);
					par1World.notifyBlockChange(par2 + var7 + 1, par3, par4, 0);
					par1World.notifyBlockChange(par2 + var7 + 2, par3, par4, 0);
					par1World.notifyBlockChange(par2 + var7, par3 - 1, par4, 0);
					par1World.notifyBlockChange(par2 + var7 + 1, par3 - 1, par4, 0);
					par1World.notifyBlockChange(par2 + var7 + 2, par3 - 1, par4, 0);
					par1World.notifyBlockChange(par2 + var7 + 1, par3 - 2, par4, 0);
					return;
				}
			}
		}
	}

	private boolean func_82528_d(World par1World, int par2, int par3, int par4, int par5) {
		if (par1World.getBlockId(par2, par3, par4) != this.blockID) {
			return false;
		} else {
			TileEntity var6 = par1World.getBlockTileEntity(par2, par3, par4);
			return var6 != null && var6 instanceof TileEntitySkull ? ((TileEntitySkull) var6).getSkullType() == par5
					: false;
		}
	}
}
