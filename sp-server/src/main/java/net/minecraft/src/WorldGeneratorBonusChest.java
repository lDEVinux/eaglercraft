package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class WorldGeneratorBonusChest extends WorldGenerator {
	/**
	 * Instance of WeightedRandomChestContent what will randomly generate items into
	 * the Bonus Chest.
	 */
	private final WeightedRandomChestContent[] theBonusChestGenerator;

	/**
	 * Value of this int will determine how much items gonna generate in Bonus
	 * Chest.
	 */
	private final int itemsToGenerateInBonusChest;

	public WorldGeneratorBonusChest(WeightedRandomChestContent[] par1ArrayOfWeightedRandomChestContent, int par2) {
		this.theBonusChestGenerator = par1ArrayOfWeightedRandomChestContent;
		this.itemsToGenerateInBonusChest = par2;
	}

	public boolean generate(World par1World, EaglercraftRandom par2Random, int par3, int par4, int par5) {
		int var12;

		for (boolean var6 = false; ((var12 = par1World.getBlockId(par3, par4, par5)) == 0
				|| var12 == Block.leaves.blockID) && par4 > 1; --par4) {
			;
		}

		if (par4 < 1) {
			return false;
		} else {
			++par4;

			for (int var7 = 0; var7 < 4; ++var7) {
				int var8 = par3 + par2Random.nextInt(4) - par2Random.nextInt(4);
				int var9 = par4 + par2Random.nextInt(3) - par2Random.nextInt(3);
				int var10 = par5 + par2Random.nextInt(4) - par2Random.nextInt(4);

				if (par1World.isAirBlock(var8, var9, var10)
						&& par1World.doesBlockHaveSolidTopSurface(var8, var9 - 1, var10)) {
					par1World.setBlock(var8, var9, var10, Block.chest.blockID, 0, 2);
					TileEntityChest var11 = (TileEntityChest) par1World.getBlockTileEntity(var8, var9, var10);

					if (var11 != null && var11 != null) {
						WeightedRandomChestContent.generateChestContents(par2Random, this.theBonusChestGenerator, var11,
								this.itemsToGenerateInBonusChest);
					}

					if (par1World.isAirBlock(var8 - 1, var9, var10)
							&& par1World.doesBlockHaveSolidTopSurface(var8 - 1, var9 - 1, var10)) {
						par1World.setBlock(var8 - 1, var9, var10, Block.torchWood.blockID, 0, 2);
					}

					if (par1World.isAirBlock(var8 + 1, var9, var10)
							&& par1World.doesBlockHaveSolidTopSurface(var8 - 1, var9 - 1, var10)) {
						par1World.setBlock(var8 + 1, var9, var10, Block.torchWood.blockID, 0, 2);
					}

					if (par1World.isAirBlock(var8, var9, var10 - 1)
							&& par1World.doesBlockHaveSolidTopSurface(var8 - 1, var9 - 1, var10)) {
						par1World.setBlock(var8, var9, var10 - 1, Block.torchWood.blockID, 0, 2);
					}

					if (par1World.isAirBlock(var8, var9, var10 + 1)
							&& par1World.doesBlockHaveSolidTopSurface(var8 - 1, var9 - 1, var10)) {
						par1World.setBlock(var8, var9, var10 + 1, Block.torchWood.blockID, 0, 2);
					}

					return true;
				}
			}

			return false;
		}
	}
}
