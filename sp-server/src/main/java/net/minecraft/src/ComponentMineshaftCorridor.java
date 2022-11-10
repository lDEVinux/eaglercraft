package net.minecraft.src;

import java.util.List;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class ComponentMineshaftCorridor extends StructureComponent {
	private final boolean hasRails;
	private final boolean hasSpiders;
	private boolean spawnerPlaced;

	/**
	 * A count of the different sections of this mine. The space between ceiling
	 * supports.
	 */
	private int sectionCount;

	public ComponentMineshaftCorridor(int par1, EaglercraftRandom par2Random, StructureBoundingBox par3StructureBoundingBox,
			int par4) {
		super(par1);
		this.coordBaseMode = par4;
		this.boundingBox = par3StructureBoundingBox;
		this.hasRails = par2Random.nextInt(3) == 0;
		this.hasSpiders = !this.hasRails && par2Random.nextInt(23) == 0;

		if (this.coordBaseMode != 2 && this.coordBaseMode != 0) {
			this.sectionCount = par3StructureBoundingBox.getXSize() / 5;
		} else {
			this.sectionCount = par3StructureBoundingBox.getZSize() / 5;
		}
	}

	public static StructureBoundingBox findValidPlacement(List par0List, EaglercraftRandom par1Random, int par2, int par3,
			int par4, int par5) {
		StructureBoundingBox var6 = new StructureBoundingBox(par2, par3, par4, par2, par3 + 2, par4);
		int var7;

		for (var7 = par1Random.nextInt(3) + 2; var7 > 0; --var7) {
			int var8 = var7 * 5;

			switch (par5) {
			case 0:
				var6.maxX = par2 + 2;
				var6.maxZ = par4 + (var8 - 1);
				break;

			case 1:
				var6.minX = par2 - (var8 - 1);
				var6.maxZ = par4 + 2;
				break;

			case 2:
				var6.maxX = par2 + 2;
				var6.minZ = par4 - (var8 - 1);
				break;

			case 3:
				var6.maxX = par2 + (var8 - 1);
				var6.maxZ = par4 + 2;
			}

			if (StructureComponent.findIntersecting(par0List, var6) == null) {
				break;
			}
		}

		return var7 > 0 ? var6 : null;
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current
	 * Location of StructGen
	 */
	public void buildComponent(StructureComponent par1StructureComponent, List par2List, EaglercraftRandom par3Random) {
		int var4 = this.getComponentType();
		int var5 = par3Random.nextInt(4);

		switch (this.coordBaseMode) {
		case 0:
			if (var5 <= 1) {
				StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random,
						this.boundingBox.minX, this.boundingBox.minY - 1 + par3Random.nextInt(3),
						this.boundingBox.maxZ + 1, this.coordBaseMode, var4);
			} else if (var5 == 2) {
				StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random,
						this.boundingBox.minX - 1, this.boundingBox.minY - 1 + par3Random.nextInt(3),
						this.boundingBox.maxZ - 3, 1, var4);
			} else {
				StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random,
						this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + par3Random.nextInt(3),
						this.boundingBox.maxZ - 3, 3, var4);
			}

			break;

		case 1:
			if (var5 <= 1) {
				StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random,
						this.boundingBox.minX - 1, this.boundingBox.minY - 1 + par3Random.nextInt(3),
						this.boundingBox.minZ, this.coordBaseMode, var4);
			} else if (var5 == 2) {
				StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random,
						this.boundingBox.minX, this.boundingBox.minY - 1 + par3Random.nextInt(3),
						this.boundingBox.minZ - 1, 2, var4);
			} else {
				StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random,
						this.boundingBox.minX, this.boundingBox.minY - 1 + par3Random.nextInt(3),
						this.boundingBox.maxZ + 1, 0, var4);
			}

			break;

		case 2:
			if (var5 <= 1) {
				StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random,
						this.boundingBox.minX, this.boundingBox.minY - 1 + par3Random.nextInt(3),
						this.boundingBox.minZ - 1, this.coordBaseMode, var4);
			} else if (var5 == 2) {
				StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random,
						this.boundingBox.minX - 1, this.boundingBox.minY - 1 + par3Random.nextInt(3),
						this.boundingBox.minZ, 1, var4);
			} else {
				StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random,
						this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + par3Random.nextInt(3),
						this.boundingBox.minZ, 3, var4);
			}

			break;

		case 3:
			if (var5 <= 1) {
				StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random,
						this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + par3Random.nextInt(3),
						this.boundingBox.minZ, this.coordBaseMode, var4);
			} else if (var5 == 2) {
				StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random,
						this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + par3Random.nextInt(3),
						this.boundingBox.minZ - 1, 2, var4);
			} else {
				StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random,
						this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + par3Random.nextInt(3),
						this.boundingBox.maxZ + 1, 0, var4);
			}
		}

		if (var4 < 8) {
			int var6;
			int var7;

			if (this.coordBaseMode != 2 && this.coordBaseMode != 0) {
				for (var6 = this.boundingBox.minX + 3; var6 + 3 <= this.boundingBox.maxX; var6 += 5) {
					var7 = par3Random.nextInt(5);

					if (var7 == 0) {
						StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, var6,
								this.boundingBox.minY, this.boundingBox.minZ - 1, 2, var4 + 1);
					} else if (var7 == 1) {
						StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, var6,
								this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, var4 + 1);
					}
				}
			} else {
				for (var6 = this.boundingBox.minZ + 3; var6 + 3 <= this.boundingBox.maxZ; var6 += 5) {
					var7 = par3Random.nextInt(5);

					if (var7 == 0) {
						StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random,
								this.boundingBox.minX - 1, this.boundingBox.minY, var6, 1, var4 + 1);
					} else if (var7 == 1) {
						StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random,
								this.boundingBox.maxX + 1, this.boundingBox.minY, var6, 3, var4 + 1);
					}
				}
			}
		}
	}

	/**
	 * Used to generate chests with items in it. ex: Temple Chests, Village
	 * Blacksmith Chests, Mineshaft Chests.
	 */
	protected boolean generateStructureChestContents(World par1World, StructureBoundingBox par2StructureBoundingBox,
			EaglercraftRandom par3Random, int par4, int par5, int par6,
			WeightedRandomChestContent[] par7ArrayOfWeightedRandomChestContent, int par8) {
		int var9 = this.getXWithOffset(par4, par6);
		int var10 = this.getYWithOffset(par5);
		int var11 = this.getZWithOffset(par4, par6);

		if (par2StructureBoundingBox.isVecInside(var9, var10, var11) && par1World.getBlockId(var9, var10, var11) == 0) {
			par1World.setBlock(var9, var10, var11, Block.rail.blockID,
					this.getMetadataWithOffset(Block.rail.blockID, par3Random.nextBoolean() ? 1 : 0), 2);
			EntityMinecartChest var12 = new EntityMinecartChest(par1World, (double) ((float) var9 + 0.5F),
					(double) ((float) var10 + 0.5F), (double) ((float) var11 + 0.5F));
			WeightedRandomChestContent.generateChestContents(par3Random, par7ArrayOfWeightedRandomChestContent, var12,
					par8);
			par1World.spawnEntityInWorld(var12);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob
	 * Spawners, it closes Mineshafts at the end, it adds Fences...
	 */
	public boolean addComponentParts(World par1World, EaglercraftRandom par2Random,
			StructureBoundingBox par3StructureBoundingBox) {
		if (this.isLiquidInStructureBoundingBox(par1World, par3StructureBoundingBox)) {
			return false;
		} else {
			int var8 = this.sectionCount * 5 - 1;
			this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 2, 1, var8, 0, 0, false);
			this.randomlyFillWithBlocks(par1World, par3StructureBoundingBox, par2Random, 0.8F, 0, 2, 0, 2, 2, var8, 0,
					0, false);

			if (this.hasSpiders) {
				this.randomlyFillWithBlocks(par1World, par3StructureBoundingBox, par2Random, 0.6F, 0, 0, 0, 2, 1, var8,
						Block.web.blockID, 0, false);
			}

			int var9;
			int var10;
			int var11;

			for (var9 = 0; var9 < this.sectionCount; ++var9) {
				var10 = 2 + var9 * 5;
				this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, var10, 0, 1, var10, Block.fence.blockID,
						0, false);
				this.fillWithBlocks(par1World, par3StructureBoundingBox, 2, 0, var10, 2, 1, var10, Block.fence.blockID,
						0, false);

				if (par2Random.nextInt(4) == 0) {
					this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 2, var10, 0, 2, var10,
							Block.planks.blockID, 0, false);
					this.fillWithBlocks(par1World, par3StructureBoundingBox, 2, 2, var10, 2, 2, var10,
							Block.planks.blockID, 0, false);
				} else {
					this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 2, var10, 2, 2, var10,
							Block.planks.blockID, 0, false);
				}

				this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.1F, 0, 2, var10 - 1,
						Block.web.blockID, 0);
				this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.1F, 2, 2, var10 - 1,
						Block.web.blockID, 0);
				this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.1F, 0, 2, var10 + 1,
						Block.web.blockID, 0);
				this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.1F, 2, 2, var10 + 1,
						Block.web.blockID, 0);
				this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.05F, 0, 2, var10 - 2,
						Block.web.blockID, 0);
				this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.05F, 2, 2, var10 - 2,
						Block.web.blockID, 0);
				this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.05F, 0, 2, var10 + 2,
						Block.web.blockID, 0);
				this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.05F, 2, 2, var10 + 2,
						Block.web.blockID, 0);
				this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.05F, 1, 2, var10 - 1,
						Block.torchWood.blockID, 0);
				this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.05F, 1, 2, var10 + 1,
						Block.torchWood.blockID, 0);

				if (par2Random.nextInt(100) == 0) {
					this.generateStructureChestContents(par1World, par3StructureBoundingBox, par2Random, 2, 0,
							var10 - 1,
							WeightedRandomChestContent.func_92080_a(StructureMineshaftPieces.func_78816_a(),
									new WeightedRandomChestContent[] { Item.enchantedBook.func_92114_b(par2Random) }),
							3 + par2Random.nextInt(4));
				}

				if (par2Random.nextInt(100) == 0) {
					this.generateStructureChestContents(par1World, par3StructureBoundingBox, par2Random, 0, 0,
							var10 + 1,
							WeightedRandomChestContent.func_92080_a(StructureMineshaftPieces.func_78816_a(),
									new WeightedRandomChestContent[] { Item.enchantedBook.func_92114_b(par2Random) }),
							3 + par2Random.nextInt(4));
				}

				if (this.hasSpiders && !this.spawnerPlaced) {
					var11 = this.getYWithOffset(0);
					int var12 = var10 - 1 + par2Random.nextInt(3);
					int var13 = this.getXWithOffset(1, var12);
					var12 = this.getZWithOffset(1, var12);

					if (par3StructureBoundingBox.isVecInside(var13, var11, var12)) {
						this.spawnerPlaced = true;
						par1World.setBlock(var13, var11, var12, Block.mobSpawner.blockID, 0, 2);
						TileEntityMobSpawner var14 = (TileEntityMobSpawner) par1World.getBlockTileEntity(var13, var11,
								var12);

						if (var14 != null) {
							var14.func_98049_a().setMobID("CaveSpider");
						}
					}
				}
			}

			for (var9 = 0; var9 <= 2; ++var9) {
				for (var10 = 0; var10 <= var8; ++var10) {
					var11 = this.getBlockIdAtCurrentPosition(par1World, var9, -1, var10, par3StructureBoundingBox);

					if (var11 == 0) {
						this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, var9, -1, var10,
								par3StructureBoundingBox);
					}
				}
			}

			if (this.hasRails) {
				for (var9 = 0; var9 <= var8; ++var9) {
					var10 = this.getBlockIdAtCurrentPosition(par1World, 1, -1, var9, par3StructureBoundingBox);

					if (var10 > 0 && Block.opaqueCubeLookup[var10]) {
						this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.7F, 1, 0, var9,
								Block.rail.blockID, this.getMetadataWithOffset(Block.rail.blockID, 0));
					}
				}
			}

			return true;
		}
	}
}
