package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockGlass extends BlockBreakable {
	public BlockGlass(int par1, Material par2Material, boolean par3) {
		super(par1, "glass", par2Material, par3);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom par1Random) {
		return 0;
	}

	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1 for
	 * alpha
	 */
	public int getRenderBlockPass() {
		return 0;
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
	 * Return true if a player with Silk Touch can harvest this block directly, and
	 * not its normal drops.
	 */
	protected boolean canSilkHarvest() {
		return true;
	}
}
