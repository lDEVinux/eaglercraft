package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class BiomeGenHills extends BiomeGenBase {
	private WorldGenerator theWorldGenerator;

	protected BiomeGenHills(int par1) {
		super(par1);
		this.theWorldGenerator = new WorldGenMinable(Block.silverfish.blockID, 8);
	}

	public void decorate(World par1World, EaglercraftRandom par2Random, int par3, int par4) {
		super.decorate(par1World, par2Random, par3, par4);
		int var5 = 3 + par2Random.nextInt(6);
		int var6;
		int var7;
		int var8;

		for (var6 = 0; var6 < var5; ++var6) {
			var7 = par3 + par2Random.nextInt(16);
			var8 = par2Random.nextInt(28) + 4;
			int var9 = par4 + par2Random.nextInt(16);
			int var10 = par1World.getBlockId(var7, var8, var9);

			if (var10 == Block.stone.blockID) {
				par1World.setBlock(var7, var8, var9, Block.oreEmerald.blockID, 0, 2);
			}
		}

		for (var5 = 0; var5 < 7; ++var5) {
			var6 = par3 + par2Random.nextInt(16);
			var7 = par2Random.nextInt(64);
			var8 = par4 + par2Random.nextInt(16);
			this.theWorldGenerator.generate(par1World, par2Random, var6, var7, var8);
		}
	}
}
