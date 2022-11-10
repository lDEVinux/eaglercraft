package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class MapGenScatteredFeature extends MapGenStructure {
	private static List biomelist = Arrays.asList(new BiomeGenBase[] { BiomeGenBase.desert, BiomeGenBase.desertHills,
			BiomeGenBase.jungle, BiomeGenBase.jungleHills, BiomeGenBase.swampland });

	/** contains possible spawns for scattered features */
	private List scatteredFeatureSpawnList;

	/** the maximum distance between scattered features */
	private int maxDistanceBetweenScatteredFeatures;

	/** the minimum distance between scattered features */
	private int minDistanceBetweenScatteredFeatures;

	public MapGenScatteredFeature() {
		this.scatteredFeatureSpawnList = new ArrayList();
		this.maxDistanceBetweenScatteredFeatures = 32;
		this.minDistanceBetweenScatteredFeatures = 8;
		this.scatteredFeatureSpawnList.add(new SpawnListEntry((w) -> new EntityWitch(w), 1, 1, 1));
	}

	public MapGenScatteredFeature(Map par1Map) {
		this();
		Iterator var2 = par1Map.entrySet().iterator();

		while (var2.hasNext()) {
			Entry var3 = (Entry) var2.next();

			if (((String) var3.getKey()).equals("distance")) {
				this.maxDistanceBetweenScatteredFeatures = MathHelper.parseIntWithDefaultAndMax(
						(String) var3.getValue(), this.maxDistanceBetweenScatteredFeatures,
						this.minDistanceBetweenScatteredFeatures + 1);
			}
		}
	}

	protected boolean canSpawnStructureAtCoords(int par1, int par2) {
		int var3 = par1;
		int var4 = par2;

		if (par1 < 0) {
			par1 -= this.maxDistanceBetweenScatteredFeatures - 1;
		}

		if (par2 < 0) {
			par2 -= this.maxDistanceBetweenScatteredFeatures - 1;
		}

		int var5 = par1 / this.maxDistanceBetweenScatteredFeatures;
		int var6 = par2 / this.maxDistanceBetweenScatteredFeatures;
		EaglercraftRandom var7 = this.worldObj.setRandomSeed(var5, var6, 14357617);
		var5 *= this.maxDistanceBetweenScatteredFeatures;
		var6 *= this.maxDistanceBetweenScatteredFeatures;
		var5 += var7.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures);
		var6 += var7.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures);

		if (var3 == var5 && var4 == var6) {
			BiomeGenBase var8 = this.worldObj.getWorldChunkManager().getBiomeGenAt(var3 * 16 + 8, var4 * 16 + 8);
			Iterator var9 = biomelist.iterator();

			while (var9.hasNext()) {
				BiomeGenBase var10 = (BiomeGenBase) var9.next();

				if (var8 == var10) {
					return true;
				}
			}
		}

		return false;
	}

	protected StructureStart getStructureStart(int par1, int par2) {
		return new StructureScatteredFeatureStart(this.worldObj, this.rand, par1, par2);
	}

	/**
	 * returns possible spawns for scattered features
	 */
	public List getScatteredFeatureSpawnList() {
		return this.scatteredFeatureSpawnList;
	}
}
