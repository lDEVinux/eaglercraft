package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockDeadBush extends BlockFlower {
	protected BlockDeadBush(int par1) {
		super(par1, Material.vine);
		float var2 = 0.4F;
		this.setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, 0.8F, 0.5F + var2);
	}

	/**
	 * Gets passed in the blockID of the block below and supposed to return true if
	 * its allowed to grow on the type of blockID passed in. Args: blockID
	 */
	protected boolean canThisPlantGrowOnThisBlockID(int par1) {
		return par1 == Block.sand.blockID;
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return -1;
	}
}
