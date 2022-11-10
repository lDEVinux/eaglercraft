package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockMushroomCap extends Block {
	private static final String[] field_94429_a = new String[] { "mushroom_skin_brown", "mushroom_skin_red" };

	/** The mushroom type. 0 for brown, 1 for red. */
	private final int mushroomType;

	public BlockMushroomCap(int par1, Material par2Material, int par3) {
		super(par1, par2Material);
		this.mushroomType = par3;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom par1Random) {
		int var2 = par1Random.nextInt(10) - 7;

		if (var2 < 0) {
			var2 = 0;
		}

		return var2;
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Block.mushroomBrown.blockID + this.mushroomType;
	}
}
