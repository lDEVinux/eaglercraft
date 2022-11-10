package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class StructureScatteredFeatureStart extends StructureStart {
	public StructureScatteredFeatureStart(World par1World, EaglercraftRandom par2Random, int par3, int par4) {
		BiomeGenBase var5 = par1World.getBiomeGenForCoords(par3 * 16 + 8, par4 * 16 + 8);

		if (var5 != BiomeGenBase.jungle && var5 != BiomeGenBase.jungleHills) {
			if (var5 == BiomeGenBase.swampland) {
				ComponentScatteredFeatureSwampHut var7 = new ComponentScatteredFeatureSwampHut(par2Random, par3 * 16,
						par4 * 16);
				this.components.add(var7);
			} else {
				ComponentScatteredFeatureDesertPyramid var8 = new ComponentScatteredFeatureDesertPyramid(par2Random,
						par3 * 16, par4 * 16);
				this.components.add(var8);
			}
		} else {
			ComponentScatteredFeatureJunglePyramid var6 = new ComponentScatteredFeatureJunglePyramid(par2Random,
					par3 * 16, par4 * 16);
			this.components.add(var6);
		}

		this.updateBoundingBox();
	}
}
