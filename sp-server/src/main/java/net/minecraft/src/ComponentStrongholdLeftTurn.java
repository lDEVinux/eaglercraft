package net.minecraft.src;

import java.util.List;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class ComponentStrongholdLeftTurn extends ComponentStronghold {
	protected final EnumDoor doorType;

	public ComponentStrongholdLeftTurn(int par1, EaglercraftRandom par2Random, StructureBoundingBox par3StructureBoundingBox,
			int par4) {
		super(par1);
		this.coordBaseMode = par4;
		this.doorType = this.getRandomDoor(par2Random);
		this.boundingBox = par3StructureBoundingBox;
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current
	 * Location of StructGen
	 */
	public void buildComponent(StructureComponent par1StructureComponent, List par2List, EaglercraftRandom par3Random) {
		if (this.coordBaseMode != 2 && this.coordBaseMode != 3) {
			this.getNextComponentZ((ComponentStrongholdStairs2) par1StructureComponent, par2List, par3Random, 1, 1);
		} else {
			this.getNextComponentX((ComponentStrongholdStairs2) par1StructureComponent, par2List, par3Random, 1, 1);
		}
	}

	public static ComponentStrongholdLeftTurn findValidPlacement(List par0List, EaglercraftRandom par1Random, int par2, int par3,
			int par4, int par5, int par6) {
		StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(par2, par3, par4, -1, -1, 0, 5, 5,
				5, par5);
		return canStrongholdGoDeeper(var7) && StructureComponent.findIntersecting(par0List, var7) == null
				? new ComponentStrongholdLeftTurn(par6, par1Random, var7, par5)
				: null;
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
			this.fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 4, 4, 4, true, par2Random,
					StructureStrongholdPieces.getStrongholdStones());
			this.placeDoor(par1World, par2Random, par3StructureBoundingBox, this.doorType, 1, 1, 0);

			if (this.coordBaseMode != 2 && this.coordBaseMode != 3) {
				this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, 1, 1, 4, 3, 3, 0, 0, false);
			} else {
				this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 1, 1, 0, 3, 3, 0, 0, false);
			}

			return true;
		}
	}
}
