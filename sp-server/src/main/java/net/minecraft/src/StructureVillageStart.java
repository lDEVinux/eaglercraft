package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

class StructureVillageStart extends StructureStart {
	/** well ... thats what it does */
	private boolean hasMoreThanTwoComponents = false;

	public StructureVillageStart(World par1World, EaglercraftRandom par2Random, int par3, int par4, int par5) {
		ArrayList var6 = StructureVillagePieces.getStructureVillageWeightedPieceList(par2Random, par5);
		ComponentVillageStartPiece var7 = new ComponentVillageStartPiece(par1World.getWorldChunkManager(), 0,
				par2Random, (par3 << 4) + 2, (par4 << 4) + 2, var6, par5);
		this.components.add(var7);
		var7.buildComponent(var7, this.components, par2Random);
		ArrayList var8 = var7.field_74930_j;
		ArrayList var9 = var7.field_74932_i;
		int var10;

		while (!var8.isEmpty() || !var9.isEmpty()) {
			StructureComponent var11;

			if (var8.isEmpty()) {
				var10 = par2Random.nextInt(var9.size());
				var11 = (StructureComponent) var9.remove(var10);
				var11.buildComponent(var7, this.components, par2Random);
			} else {
				var10 = par2Random.nextInt(var8.size());
				var11 = (StructureComponent) var8.remove(var10);
				var11.buildComponent(var7, this.components, par2Random);
			}
		}

		this.updateBoundingBox();
		var10 = 0;
		Iterator var13 = this.components.iterator();

		while (var13.hasNext()) {
			StructureComponent var12 = (StructureComponent) var13.next();

			if (!(var12 instanceof ComponentVillageRoadPiece)) {
				++var10;
			}
		}

		this.hasMoreThanTwoComponents = var10 > 2;
	}

	/**
	 * currently only defined for Villages, returns true if Village has more than 2
	 * non-road components
	 */
	public boolean isSizeableStructure() {
		return this.hasMoreThanTwoComponents;
	}
}
