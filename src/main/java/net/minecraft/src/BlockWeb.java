package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockWeb extends Block {
	public BlockWeb(int par1) {
		super(par1, Material.web);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**
	 * Triggered whenever an entity collides with this block (enters into the
	 * block). Args: world, x, y, z, entity
	 */
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
		par5Entity.setInWeb();
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
	 * Returns a bounding box from the pool of bounding boxes (this means this box
	 * can change after the pool has been cleared to be reused)
	 */
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 1;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Item.silk.itemID;
	}

	/**
	 * Return true if a player with Silk Touch can harvest this block directly, and
	 * not its normal drops.
	 */
	protected boolean canSilkHarvest() {
		return true;
	}
}
