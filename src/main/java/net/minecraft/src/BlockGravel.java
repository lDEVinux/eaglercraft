package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockGravel extends BlockSand {
	public BlockGravel(int par1) {
		super(par1);
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		if (par3 > 3) {
			par3 = 3;
		}

		return par2Random.nextInt(10 - par3 * 3) == 0 ? Item.flint.itemID : this.blockID;
	}
}
