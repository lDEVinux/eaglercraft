package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockMelon extends Block {
	protected BlockMelon(int par1) {
		super(par1, Material.pumpkin);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Item.melon.itemID;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom par1Random) {
		return 3 + par1Random.nextInt(5);
	}

	/**
	 * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i'
	 * (inclusive).
	 */
	public int quantityDroppedWithBonus(int par1, EaglercraftRandom par2Random) {
		int var3 = this.quantityDropped(par2Random) + par2Random.nextInt(1 + par1);

		if (var3 > 9) {
			var3 = 9;
		}

		return var3;
	}
}
