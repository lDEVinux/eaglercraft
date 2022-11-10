package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BlockGrass extends Block {
	protected BlockGrass(int par1) {
		super(par1, Material.grass);
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		if (!par1World.isRemote) {
			if (par1World.getBlockLightValue(par2, par3 + 1, par4) < 4
					&& Block.lightOpacity[par1World.getBlockId(par2, par3 + 1, par4)] > 2) {
				par1World.setBlock(par2, par3, par4, Block.dirt.blockID);
			} else if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9) {
				for (int var6 = 0; var6 < 4; ++var6) {
					int var7 = par2 + par5Random.nextInt(3) - 1;
					int var8 = par3 + par5Random.nextInt(5) - 3;
					int var9 = par4 + par5Random.nextInt(3) - 1;
					int var10 = par1World.getBlockId(var7, var8 + 1, var9);

					if (par1World.getBlockId(var7, var8, var9) == Block.dirt.blockID
							&& par1World.getBlockLightValue(var7, var8 + 1, var9) >= 4
							&& Block.lightOpacity[var10] <= 2) {
						par1World.setBlock(var7, var8, var9, Block.grass.blockID);
					}
				}
			}
		}
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public int idDropped(int par1, EaglercraftRandom par2Random, int par3) {
		return Block.dirt.idDropped(0, par2Random, par3);
	}
}
