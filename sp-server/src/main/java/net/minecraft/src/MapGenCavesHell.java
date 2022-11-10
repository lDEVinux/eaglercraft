package net.minecraft.src;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public class MapGenCavesHell extends MapGenBase {
	/**
	 * Generates a larger initial cave node than usual. Called 25% of the time.
	 */
	protected void generateLargeCaveNode(long par1, int par3, int par4, byte[] par5ArrayOfByte, double par6,
			double par8, double par10) {
		this.generateCaveNode(par1, par3, par4, par5ArrayOfByte, par6, par8, par10, 1.0F + this.rand.nextFloat() * 6.0F,
				0.0F, 0.0F, -1, -1, 0.5D);
	}

	/**
	 * Generates a node in the current cave system recursion tree.
	 */
	protected void generateCaveNode(long par1, int par3, int par4, byte[] par5ArrayOfByte, double par6, double par8,
			double par10, float par12, float par13, float par14, int par15, int par16, double par17) {
		double var19 = (double) (par3 * 16 + 8);
		double var21 = (double) (par4 * 16 + 8);
		float var23 = 0.0F;
		float var24 = 0.0F;
		EaglercraftRandom var25 = new EaglercraftRandom(par1);

		if (par16 <= 0) {
			int var26 = this.range * 16 - 16;
			par16 = var26 - var25.nextInt(var26 / 4);
		}

		boolean var53 = false;

		if (par15 == -1) {
			par15 = par16 / 2;
			var53 = true;
		}

		int var27 = var25.nextInt(par16 / 2) + par16 / 4;

		for (boolean var28 = var25.nextInt(6) == 0; par15 < par16; ++par15) {
			double var29 = 1.5D
					+ (double) (MathHelper.sin((float) par15 * (float) Math.PI / (float) par16) * par12 * 1.0F);
			double var31 = var29 * par17;
			float var33 = MathHelper.cos(par14);
			float var34 = MathHelper.sin(par14);
			par6 += (double) (MathHelper.cos(par13) * var33);
			par8 += (double) var34;
			par10 += (double) (MathHelper.sin(par13) * var33);

			if (var28) {
				par14 *= 0.92F;
			} else {
				par14 *= 0.7F;
			}

			par14 += var24 * 0.1F;
			par13 += var23 * 0.1F;
			var24 *= 0.9F;
			var23 *= 0.75F;
			var24 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 2.0F;
			var23 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 4.0F;

			if (!var53 && par15 == var27 && par12 > 1.0F) {
				this.generateCaveNode(var25.nextLong(), par3, par4, par5ArrayOfByte, par6, par8, par10,
						var25.nextFloat() * 0.5F + 0.5F, par13 - ((float) Math.PI / 2F), par14 / 3.0F, par15, par16,
						1.0D);
				this.generateCaveNode(var25.nextLong(), par3, par4, par5ArrayOfByte, par6, par8, par10,
						var25.nextFloat() * 0.5F + 0.5F, par13 + ((float) Math.PI / 2F), par14 / 3.0F, par15, par16,
						1.0D);
				return;
			}

			if (var53 || var25.nextInt(4) != 0) {
				double var35 = par6 - var19;
				double var37 = par10 - var21;
				double var39 = (double) (par16 - par15);
				double var41 = (double) (par12 + 2.0F + 16.0F);

				if (var35 * var35 + var37 * var37 - var39 * var39 > var41 * var41) {
					return;
				}

				if (par6 >= var19 - 16.0D - var29 * 2.0D && par10 >= var21 - 16.0D - var29 * 2.0D
						&& par6 <= var19 + 16.0D + var29 * 2.0D && par10 <= var21 + 16.0D + var29 * 2.0D) {
					int var54 = MathHelper.floor_double(par6 - var29) - par3 * 16 - 1;
					int var36 = MathHelper.floor_double(par6 + var29) - par3 * 16 + 1;
					int var55 = MathHelper.floor_double(par8 - var31) - 1;
					int var38 = MathHelper.floor_double(par8 + var31) + 1;
					int var56 = MathHelper.floor_double(par10 - var29) - par4 * 16 - 1;
					int var40 = MathHelper.floor_double(par10 + var29) - par4 * 16 + 1;

					if (var54 < 0) {
						var54 = 0;
					}

					if (var36 > 16) {
						var36 = 16;
					}

					if (var55 < 1) {
						var55 = 1;
					}

					if (var38 > 120) {
						var38 = 120;
					}

					if (var56 < 0) {
						var56 = 0;
					}

					if (var40 > 16) {
						var40 = 16;
					}

					boolean var57 = false;
					int var42;
					int var45;

					for (var42 = var54; !var57 && var42 < var36; ++var42) {
						for (int var43 = var56; !var57 && var43 < var40; ++var43) {
							for (int var44 = var38 + 1; !var57 && var44 >= var55 - 1; --var44) {
								var45 = (var42 * 16 + var43) * 128 + var44;

								if (var44 >= 0 && var44 < 128) {
									if (par5ArrayOfByte[var45] == Block.lavaMoving.blockID
											|| par5ArrayOfByte[var45] == Block.lavaStill.blockID) {
										var57 = true;
									}

									if (var44 != var55 - 1 && var42 != var54 && var42 != var36 - 1 && var43 != var56
											&& var43 != var40 - 1) {
										var44 = var55;
									}
								}
							}
						}
					}

					if (!var57) {
						for (var42 = var54; var42 < var36; ++var42) {
							double var58 = ((double) (var42 + par3 * 16) + 0.5D - par6) / var29;

							for (var45 = var56; var45 < var40; ++var45) {
								double var46 = ((double) (var45 + par4 * 16) + 0.5D - par10) / var29;
								int var48 = (var42 * 16 + var45) * 128 + var38;

								for (int var49 = var38 - 1; var49 >= var55; --var49) {
									double var50 = ((double) var49 + 0.5D - par8) / var31;

									if (var50 > -0.7D && var58 * var58 + var50 * var50 + var46 * var46 < 1.0D) {
										byte var52 = par5ArrayOfByte[var48];

										if (var52 == Block.netherrack.blockID || var52 == Block.dirt.blockID
												|| var52 == Block.grass.blockID) {
											par5ArrayOfByte[var48] = 0;
										}
									}

									--var48;
								}
							}
						}

						if (var53) {
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Recursively called by generate() (generate) and optionally by itself.
	 */
	protected void recursiveGenerate(World par1World, int par2, int par3, int par4, int par5, byte[] par6ArrayOfByte) {
		int var7 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(10) + 1) + 1);

		if (this.rand.nextInt(5) != 0) {
			var7 = 0;
		}

		for (int var8 = 0; var8 < var7; ++var8) {
			double var9 = (double) (par2 * 16 + this.rand.nextInt(16));
			double var11 = (double) this.rand.nextInt(128);
			double var13 = (double) (par3 * 16 + this.rand.nextInt(16));
			int var15 = 1;

			if (this.rand.nextInt(4) == 0) {
				this.generateLargeCaveNode(this.rand.nextLong(), par4, par5, par6ArrayOfByte, var9, var11, var13);
				var15 += this.rand.nextInt(4);
			}

			for (int var16 = 0; var16 < var15; ++var16) {
				float var17 = this.rand.nextFloat() * (float) Math.PI * 2.0F;
				float var18 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
				float var19 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();
				this.generateCaveNode(this.rand.nextLong(), par4, par5, par6ArrayOfByte, var9, var11, var13,
						var19 * 2.0F, var17, var18, 0, 0, 0.5D);
			}
		}
	}
}
