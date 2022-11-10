package net.minecraft.src;

import java.util.List;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockEndPortal extends BlockContainer {
	/**
	 * true if the enderdragon has been killed - allows end portal blocks to be
	 * created in the end
	 */
	public static boolean bossDefeated = false;

	protected BlockEndPortal(int par1, Material par2Material) {
		super(par1, par2Material);
		this.setLightValue(1.0F);
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the
	 * block.
	 */
	public TileEntity createNewTileEntity(World par1World) {
		return new TileEntityEndPortal();
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		float var5 = 0.0625F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var5, 1.0F);
	}

	/**
	 * Returns true if the given side of this block type should be rendered, if the
	 * adjacent block is at the given coordinates. Args: blockAccess, x, y, z, side
	 */
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return par5 != 0 ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
	}

	/**
	 * Adds all intersecting collision boxes to a list. (Be sure to only add boxes
	 * to the list if they intersect the mask.) Parameters: World, X, Y, Z, mask,
	 * list, colliding entity
	 */
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
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
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom par1Random) {
		return 0;
	}

	/**
	 * A randomly called display update to be able to add particles or other items
	 * for display
	 */
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		double var6 = (double) ((float) par2 + par5Random.nextFloat());
		double var8 = (double) ((float) par3 + 0.8F);
		double var10 = (double) ((float) par4 + par5Random.nextFloat());
		double var12 = 0.0D;
		double var14 = 0.0D;
		double var16 = 0.0D;
		par1World.spawnParticle("smoke", var6, var8, var10, var12, var14, var16);
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return -1;
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		if (!bossDefeated) {
			if (par1World.provider.dimensionId != 0) {
				par1World.setBlockToAir(par2, par3, par4);
			}
		}
	}

	/**
	 * only called by clickMiddleMouseButton , and passed to
	 * inventory.setCurrentItem (along with isCreative)
	 */
	public int idPicked(World par1World, int par2, int par3, int par4) {
		return 0;
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("portal");
	}
}
