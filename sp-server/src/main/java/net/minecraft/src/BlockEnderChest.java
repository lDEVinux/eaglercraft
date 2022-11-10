package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockEnderChest extends BlockContainer {
	protected BlockEnderChest(int par1) {
		super(par1, Material.rock);
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
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Block.obsidian.blockID;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom par1Random) {
		return 8;
	}

	/**
	 * Return true if a player with Silk Touch can harvest this block directly, and
	 * not its normal drops.
	 */
	protected boolean canSilkHarvest() {
		return true;
	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving,
			ItemStack par6ItemStack) {
		byte var7 = 0;
		int var8 = MathHelper.floor_double((double) (par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (var8 == 0) {
			var7 = 2;
		}

		if (var8 == 1) {
			var7 = 5;
		}

		if (var8 == 2) {
			var7 = 3;
		}

		if (var8 == 3) {
			var7 = 4;
		}

		par1World.setBlockMetadata(par2, par3, par4, var7, 2);
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer,
			int par6, float par7, float par8, float par9) {
		InventoryEnderChest var10 = par5EntityPlayer.getInventoryEnderChest();
		TileEntityEnderChest var11 = (TileEntityEnderChest) par1World.getBlockTileEntity(par2, par3, par4);

		if (var10 != null && var11 != null) {
			if (par1World.isBlockNormalCube(par2, par3 + 1, par4)) {
				return true;
			} else if (par1World.isRemote) {
				return true;
			} else {
				var10.setAssociatedChest(var11);
				par5EntityPlayer.displayGUIChest(var10);
				return true;
			}
		} else {
			return true;
		}
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the
	 * block.
	 */
	public TileEntity createNewTileEntity(World par1World) {
		return new TileEntityEnderChest();
	}
}
