package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockObsidian extends BlockStone {
	public BlockObsidian(int par1) {
		super(par1);
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom par1Random) {
		return 1;
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Block.obsidian.blockID;
	}
}
