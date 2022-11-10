package net.minecraft.src;

import java.util.List;

public class BlockWall extends Block {
	/** The types of the wall. */
	public static final String[] types = new String[] { "normal", "mossy" };

	public BlockWall(int par1, Block par2Block) {
		super(par1, par2Block.blockMaterial);
		this.setHardness(par2Block.blockHardness);
		this.setResistance(par2Block.blockResistance / 3.0F);
		this.setStepSound(par2Block.stepSound);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		return par2 == 1 ? Block.cobblestoneMossy.getBlockTextureFromSide(par1) : Block.cobblestone.getBlockTextureFromSide(par1);
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 32;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
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
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		boolean var5 = this.canConnectWallTo(par1IBlockAccess, par2, par3, par4 - 1);
		boolean var6 = this.canConnectWallTo(par1IBlockAccess, par2, par3, par4 + 1);
		boolean var7 = this.canConnectWallTo(par1IBlockAccess, par2 - 1, par3, par4);
		boolean var8 = this.canConnectWallTo(par1IBlockAccess, par2 + 1, par3, par4);
		float var9 = 0.25F;
		float var10 = 0.75F;
		float var11 = 0.25F;
		float var12 = 0.75F;
		float var13 = 1.0F;

		if (var5) {
			var11 = 0.0F;
		}

		if (var6) {
			var12 = 1.0F;
		}

		if (var7) {
			var9 = 0.0F;
		}

		if (var8) {
			var10 = 1.0F;
		}

		if (var5 && var6 && !var7 && !var8) {
			var13 = 0.8125F;
			var9 = 0.3125F;
			var10 = 0.6875F;
		} else if (!var5 && !var6 && var7 && var8) {
			var13 = 0.8125F;
			var11 = 0.3125F;
			var12 = 0.6875F;
		}

		this.setBlockBounds(var9, 0.0F, var11, var10, var13, var12);
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this box
	 * can change after the pool has been cleared to be reused)
	 */
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
		this.maxY = 1.5D;
		return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
	}

	/**
	 * Return whether an adjacent block can connect to a wall.
	 */
	public boolean canConnectWallTo(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		int var5 = par1IBlockAccess.getBlockId(par2, par3, par4);

		if (var5 != this.blockID && var5 != Block.fenceGate.blockID) {
			Block var6 = Block.blocksList[var5];
			return var6 != null && var6.blockMaterial.isOpaque() && var6.renderAsNormalBlock() ? var6.blockMaterial != Material.pumpkin : false;
		} else {
			return true;
		}
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and wood.
	 */
	public int damageDropped(int par1) {
		return par1;
	}

	/**
	 * Returns true if the given side of this block type should be rendered, if the
	 * adjacent block is at the given coordinates. Args: blockAccess, x, y, z, side
	 */
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return par5 == 0 ? super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5) : true;
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
	}
}
