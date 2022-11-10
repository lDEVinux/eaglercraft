package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockStone extends Block {
	public BlockStone(int par1) {
		super(par1, Material.rock);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Block.cobblestone.blockID;
	}
}
