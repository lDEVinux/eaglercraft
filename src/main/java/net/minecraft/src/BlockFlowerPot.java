package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockFlowerPot extends Block {
	public BlockFlowerPot(int par1) {
		super(par1, Material.circuits);
		this.setBlockBoundsForItemRender();
	}

	/**
	 * Sets the block's bounds for rendering it as an item
	 */
	public void setBlockBoundsForItemRender() {
		float var1 = 0.375F;
		float var2 = var1 / 2.0F;
		this.setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, var1, 0.5F + var2);
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
		return 33;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		ItemStack var10 = par5EntityPlayer.inventory.getCurrentItem();

		if (var10 == null) {
			return false;
		} else if (par1World.getBlockMetadata(par2, par3, par4) != 0) {
			return false;
		} else {
			int var11 = getMetaForPlant(var10);

			if (var11 > 0) {
				par1World.setBlockMetadataWithNotify(par2, par3, par4, var11, 2);

				if (!par5EntityPlayer.capabilities.isCreativeMode && --var10.stackSize <= 0) {
					par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, (ItemStack) null);
				}

				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * only called by clickMiddleMouseButton , and passed to
	 * inventory.setCurrentItem (along with isCreative)
	 */
	public int idPicked(World par1World, int par2, int par3, int par4) {
		ItemStack var5 = getPlantForMeta(par1World.getBlockMetadata(par2, par3, par4));
		return var5 == null ? Item.flowerPot.itemID : var5.itemID;
	}

	/**
	 * Get the block's damage value (for use with pick block).
	 */
	public int getDamageValue(World par1World, int par2, int par3, int par4) {
		ItemStack var5 = getPlantForMeta(par1World.getBlockMetadata(par2, par3, par4));
		return var5 == null ? Item.flowerPot.itemID : var5.getItemDamage();
	}

	/**
	 * Returns true only if block is flowerPot
	 */
	public boolean isFlowerPot() {
		return true;
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates.
	 * Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		return super.canPlaceBlockAt(par1World, par2, par3, par4) && par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4);
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * blockID
	 */
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4)) {
			this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
			par1World.setBlockToAir(par2, par3, par4);
		}
	}

	/**
	 * Drops the block items with a specified chance of dropping the specified items
	 */
	public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
		super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, par7);

		if (par5 > 0) {
			ItemStack var8 = getPlantForMeta(par5);

			if (var8 != null) {
				this.dropBlockAsItem_do(par1World, par2, par3, par4, var8);
			}
		}
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Item.flowerPot.itemID;
	}

	/**
	 * Return the item associated with the specified flower pot metadata value.
	 */
	public static ItemStack getPlantForMeta(int par0) {
		switch (par0) {
		case 1:
			return new ItemStack(Block.plantRed);

		case 2:
			return new ItemStack(Block.plantYellow);

		case 3:
			return new ItemStack(Block.sapling, 1, 0);

		case 4:
			return new ItemStack(Block.sapling, 1, 1);

		case 5:
			return new ItemStack(Block.sapling, 1, 2);

		case 6:
			return new ItemStack(Block.sapling, 1, 3);

		case 7:
			return new ItemStack(Block.mushroomRed);

		case 8:
			return new ItemStack(Block.mushroomBrown);

		case 9:
			return new ItemStack(Block.cactus);

		case 10:
			return new ItemStack(Block.deadBush);

		case 11:
			return new ItemStack(Block.tallGrass, 1, 2);

		default:
			return null;
		}
	}

	/**
	 * Return the flower pot metadata value associated with the specified item.
	 */
	public static int getMetaForPlant(ItemStack par0ItemStack) {
		int var1 = par0ItemStack.getItem().itemID;

		if (var1 == Block.plantRed.blockID) {
			return 1;
		} else if (var1 == Block.plantYellow.blockID) {
			return 2;
		} else if (var1 == Block.cactus.blockID) {
			return 9;
		} else if (var1 == Block.mushroomBrown.blockID) {
			return 8;
		} else if (var1 == Block.mushroomRed.blockID) {
			return 7;
		} else if (var1 == Block.deadBush.blockID) {
			return 10;
		} else {
			if (var1 == Block.sapling.blockID) {
				switch (par0ItemStack.getItemDamage()) {
				case 0:
					return 3;

				case 1:
					return 4;

				case 2:
					return 5;

				case 3:
					return 6;
				}
			}

			if (var1 == Block.tallGrass.blockID) {
				switch (par0ItemStack.getItemDamage()) {
				case 2:
					return 11;
				}
			}

			return 0;
		}
	}
}
