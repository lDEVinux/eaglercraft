package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class ComponentScatteredFeatureSwampHut extends ComponentScatteredFeature {
	/** Whether this swamp hut has a witch. */
	private boolean hasWitch;

	public ComponentScatteredFeatureSwampHut(EaglercraftRandom par1Random, int par2, int par3) {
		super(par1Random, par2, 64, par3, 7, 5, 9);
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob
	 * Spawners, it closes Mineshafts at the end, it adds Fences...
	 */
	public boolean addComponentParts(World par1World, EaglercraftRandom par2Random,
			StructureBoundingBox par3StructureBoundingBox) {
		if (!this.func_74935_a(par1World, par3StructureBoundingBox, 0)) {
			return false;
		} else {
			this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 1, 1, 1, 5, 1, 7, Block.planks.blockID, 1,
					Block.planks.blockID, 1, false);
			this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 1, 4, 2, 5, 4, 7, Block.planks.blockID, 1,
					Block.planks.blockID, 1, false);
			this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 2, 1, 0, 4, 1, 0, Block.planks.blockID, 1,
					Block.planks.blockID, 1, false);
			this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 2, 2, 2, 3, 3, 2, Block.planks.blockID, 1,
					Block.planks.blockID, 1, false);
			this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 1, 2, 3, 1, 3, 6, Block.planks.blockID, 1,
					Block.planks.blockID, 1, false);
			this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 5, 2, 3, 5, 3, 6, Block.planks.blockID, 1,
					Block.planks.blockID, 1, false);
			this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 2, 2, 7, 4, 3, 7, Block.planks.blockID, 1,
					Block.planks.blockID, 1, false);
			this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 2, 1, 3, 2, Block.wood.blockID,
					Block.wood.blockID, false);
			this.fillWithBlocks(par1World, par3StructureBoundingBox, 5, 0, 2, 5, 3, 2, Block.wood.blockID,
					Block.wood.blockID, false);
			this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 7, 1, 3, 7, Block.wood.blockID,
					Block.wood.blockID, false);
			this.fillWithBlocks(par1World, par3StructureBoundingBox, 5, 0, 7, 5, 3, 7, Block.wood.blockID,
					Block.wood.blockID, false);
			this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 2, 3, 2, par3StructureBoundingBox);
			this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 3, 3, 7, par3StructureBoundingBox);
			this.placeBlockAtCurrentPosition(par1World, 0, 0, 1, 3, 4, par3StructureBoundingBox);
			this.placeBlockAtCurrentPosition(par1World, 0, 0, 5, 3, 4, par3StructureBoundingBox);
			this.placeBlockAtCurrentPosition(par1World, 0, 0, 5, 3, 5, par3StructureBoundingBox);
			this.placeBlockAtCurrentPosition(par1World, Block.flowerPot.blockID, 7, 1, 3, 5, par3StructureBoundingBox);
			this.placeBlockAtCurrentPosition(par1World, Block.workbench.blockID, 0, 3, 2, 6, par3StructureBoundingBox);
			this.placeBlockAtCurrentPosition(par1World, Block.cauldron.blockID, 0, 4, 2, 6, par3StructureBoundingBox);
			this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 1, 2, 1, par3StructureBoundingBox);
			this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 5, 2, 1, par3StructureBoundingBox);
			int var4 = this.getMetadataWithOffset(Block.stairsWoodOak.blockID, 3);
			int var5 = this.getMetadataWithOffset(Block.stairsWoodOak.blockID, 1);
			int var6 = this.getMetadataWithOffset(Block.stairsWoodOak.blockID, 0);
			int var7 = this.getMetadataWithOffset(Block.stairsWoodOak.blockID, 2);
			this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 0, 4, 1, 6, 4, 1,
					Block.stairsWoodSpruce.blockID, var4, Block.stairsWoodSpruce.blockID, var4, false);
			this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 0, 4, 2, 0, 4, 7,
					Block.stairsWoodSpruce.blockID, var6, Block.stairsWoodSpruce.blockID, var6, false);
			this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 6, 4, 2, 6, 4, 7,
					Block.stairsWoodSpruce.blockID, var5, Block.stairsWoodSpruce.blockID, var5, false);
			this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 0, 4, 8, 6, 4, 8,
					Block.stairsWoodSpruce.blockID, var7, Block.stairsWoodSpruce.blockID, var7, false);
			int var8;
			int var9;

			for (var8 = 2; var8 <= 7; var8 += 5) {
				for (var9 = 1; var9 <= 5; var9 += 4) {
					this.fillCurrentPositionBlocksDownwards(par1World, Block.wood.blockID, 0, var9, -1, var8,
							par3StructureBoundingBox);
				}
			}

			if (!this.hasWitch) {
				var8 = this.getXWithOffset(2, 5);
				var9 = this.getYWithOffset(2);
				int var10 = this.getZWithOffset(2, 5);

				if (par3StructureBoundingBox.isVecInside(var8, var9, var10)) {
					this.hasWitch = true;
					EntityWitch var11 = new EntityWitch(par1World);
					var11.setLocationAndAngles((double) var8 + 0.5D, (double) var9, (double) var10 + 0.5D, 0.0F, 0.0F);
					var11.initCreature();
					par1World.spawnEntityInWorld(var11);
				}
			}

			return true;
		}
	}
}
