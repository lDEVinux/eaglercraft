package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockVine extends Block {
	public BlockVine(int par1) {
		super(par1, Material.vine);
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**
	 * Sets the block's bounds for rendering it as an item
	 */
	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 20;
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
		int var6 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
		float var7 = 1.0F;
		float var8 = 1.0F;
		float var9 = 1.0F;
		float var10 = 0.0F;
		float var11 = 0.0F;
		float var12 = 0.0F;
		boolean var13 = var6 > 0;

		if ((var6 & 2) != 0) {
			var10 = Math.max(var10, 0.0625F);
			var7 = 0.0F;
			var8 = 0.0F;
			var11 = 1.0F;
			var9 = 0.0F;
			var12 = 1.0F;
			var13 = true;
		}

		if ((var6 & 8) != 0) {
			var7 = Math.min(var7, 0.9375F);
			var10 = 1.0F;
			var8 = 0.0F;
			var11 = 1.0F;
			var9 = 0.0F;
			var12 = 1.0F;
			var13 = true;
		}

		if ((var6 & 4) != 0) {
			var12 = Math.max(var12, 0.0625F);
			var9 = 0.0F;
			var7 = 0.0F;
			var10 = 1.0F;
			var8 = 0.0F;
			var11 = 1.0F;
			var13 = true;
		}

		if ((var6 & 1) != 0) {
			var9 = Math.min(var9, 0.9375F);
			var12 = 1.0F;
			var7 = 0.0F;
			var10 = 1.0F;
			var8 = 0.0F;
			var11 = 1.0F;
			var13 = true;
		}

		if (!var13 && this.canBePlacedOn(par1IBlockAccess.getBlockId(par2, par3 + 1, par4))) {
			var8 = Math.min(var8, 0.9375F);
			var11 = 1.0F;
			var7 = 0.0F;
			var10 = 1.0F;
			var9 = 0.0F;
			var12 = 1.0F;
		}

		this.setBlockBounds(var7, var8, var9, var10, var11, var12);
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this box
	 * can change after the pool has been cleared to be reused)
	 */
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
	}

	/**
	 * checks to see if you can place this block can be placed on that side of a
	 * block: BlockLever overrides
	 */
	public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5) {
		switch (par5) {
		case 1:
			return this.canBePlacedOn(par1World.getBlockId(par2, par3 + 1, par4));

		case 2:
			return this.canBePlacedOn(par1World.getBlockId(par2, par3, par4 + 1));

		case 3:
			return this.canBePlacedOn(par1World.getBlockId(par2, par3, par4 - 1));

		case 4:
			return this.canBePlacedOn(par1World.getBlockId(par2 + 1, par3, par4));

		case 5:
			return this.canBePlacedOn(par1World.getBlockId(par2 - 1, par3, par4));

		default:
			return false;
		}
	}

	/**
	 * returns true if a vine can be placed on that block (checks for render as
	 * normal block and if it is solid)
	 */
	private boolean canBePlacedOn(int par1) {
		if (par1 == 0) {
			return false;
		} else {
			Block var2 = Block.blocksList[par1];
			return var2.renderAsNormalBlock() && var2.blockMaterial.blocksMovement();
		}
	}

	/**
	 * Returns if the vine can stay in the world. It also changes the metadata
	 * according to neighboring blocks.
	 */
	private boolean canVineStay(World par1World, int par2, int par3, int par4) {
		int var5 = par1World.getBlockMetadata(par2, par3, par4);
		int var6 = var5;

		if (var5 > 0) {
			for (int var7 = 0; var7 <= 3; ++var7) {
				int var8 = 1 << var7;

				if ((var5 & var8) != 0 && !this.canBePlacedOn(par1World.getBlockId(par2 + Direction.offsetX[var7], par3, par4 + Direction.offsetZ[var7]))
						&& (par1World.getBlockId(par2, par3 + 1, par4) != this.blockID || (par1World.getBlockMetadata(par2, par3 + 1, par4) & var8) == 0)) {
					var6 &= ~var8;
				}
			}
		}

		if (var6 == 0 && !this.canBePlacedOn(par1World.getBlockId(par2, par3 + 1, par4))) {
			return false;
		} else {
			if (var6 != var5) {
				par1World.setBlockMetadataWithNotify(par2, par3, par4, var6, 2);
			}

			return true;
		}
	}

	public int getBlockColor() {
		return ColorizerFoliage.getFoliageColorBasic();
	}

	/**
	 * Returns the color this block should be rendered. Used by leaves.
	 */
	public int getRenderColor(int par1) {
		return ColorizerFoliage.getFoliageColorBasic();
	}

	/**
	 * Returns a integer with hex for 0xrrggbb with this color multiplied against
	 * the blocks color. Note only called when first determining what to render.
	 */
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		int var5 = par1IBlockAccess.getBiomeGenForCoords(par2, par4).getBiomeFoliageColor();
		
		initNoiseField(par2 >> 4, par4 >> 4);
		float noise = (float)(grassNoiseArray[(par4 & 15) + (par2 & 15) * 16]) * 0.25F + 1.0F;
		int var6 = (int)(((var5 >> 8) & 255) * noise);
		if(var6 > 255) var6 = 255;
		if(var6 < 0) var6 = 0;
		
		return (var5 & 0xff00ff) | (var6 << 8);
	}

	/**
	 * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z,
	 * side, hitX, hitY, hitZ, block metadata
	 */
	public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9) {
		byte var10 = 0;

		switch (par5) {
		case 2:
			var10 = 1;
			break;

		case 3:
			var10 = 4;
			break;

		case 4:
			var10 = 8;
			break;

		case 5:
			var10 = 2;
		}

		return var10 != 0 ? var10 : par9;
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return 0;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom par1Random) {
		return 0;
	}
}
