package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

abstract class ComponentScatteredFeature extends StructureComponent {
	/** The size of the bounding box for this feature in the X axis */
	protected final int scatteredFeatureSizeX;

	/** The size of the bounding box for this feature in the Y axis */
	protected final int scatteredFeatureSizeY;

	/** The size of the bounding box for this feature in the Z axis */
	protected final int scatteredFeatureSizeZ;
	protected int field_74936_d = -1;

	protected ComponentScatteredFeature(EaglercraftRandom par1Random, int par2, int par3, int par4, int par5, int par6, int par7) {
		super(0);
		this.scatteredFeatureSizeX = par5;
		this.scatteredFeatureSizeY = par6;
		this.scatteredFeatureSizeZ = par7;
		this.coordBaseMode = par1Random.nextInt(4);

		switch (this.coordBaseMode) {
		case 0:
		case 2:
			this.boundingBox = new StructureBoundingBox(par2, par3, par4, par2 + par5 - 1, par3 + par6 - 1,
					par4 + par7 - 1);
			break;

		default:
			this.boundingBox = new StructureBoundingBox(par2, par3, par4, par2 + par7 - 1, par3 + par6 - 1,
					par4 + par5 - 1);
		}
	}

	protected boolean func_74935_a(World par1World, StructureBoundingBox par2StructureBoundingBox, int par3) {
		if (this.field_74936_d >= 0) {
			return true;
		} else {
			int var4 = 0;
			int var5 = 0;

			for (int var6 = this.boundingBox.minZ; var6 <= this.boundingBox.maxZ; ++var6) {
				for (int var7 = this.boundingBox.minX; var7 <= this.boundingBox.maxX; ++var7) {
					if (par2StructureBoundingBox.isVecInside(var7, 64, var6)) {
						var4 += Math.max(par1World.getTopSolidOrLiquidBlock(var7, var6),
								par1World.provider.getAverageGroundLevel());
						++var5;
					}
				}
			}

			if (var5 == 0) {
				return false;
			} else {
				this.field_74936_d = var4 / var5;
				this.boundingBox.offset(0, this.field_74936_d - this.boundingBox.minY + par3, 0);
				return true;
			}
		}
	}
}
