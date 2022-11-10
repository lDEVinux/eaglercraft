package net.minecraft.src;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.lax1dude.eaglercraft.sp.EaglercraftRandom;

public final class SpawnerAnimals {
	/** The 17x17 area around the player where mobs can spawn */
	private static HashMap eligibleChunksForSpawning = new HashMap();

	/** An array of entity classes that spawn at night. */
	protected static final Class[] nightSpawnEntities = new Class[] { EntitySpider.class, EntityZombie.class,
			EntitySkeleton.class };

	/**
	 * Given a chunk, find a random position in it.
	 */
	protected static ChunkPosition getRandomSpawningPointInChunk(World par0World, int par1, int par2) {
		Chunk var3 = par0World.getChunkFromChunkCoords(par1, par2);
		int var4 = par1 * 16 + par0World.rand.nextInt(16);
		int var5 = par2 * 16 + par0World.rand.nextInt(16);
		int var6 = par0World.rand
				.nextInt(var3 == null ? par0World.getActualHeight() : var3.getTopFilledSegment() + 16 - 1);
		return new ChunkPosition(var4, var6, var5);
	}

	/**
	 * adds all chunks within the spawn radius of the players to
	 * eligibleChunksForSpawning. pars: the world, hostileCreatures,
	 * passiveCreatures. returns number of eligible chunks.
	 */
	public static final int findChunksForSpawning(WorldServer par0WorldServer, boolean par1, boolean par2,
			boolean par3) {
		if (!par1 && !par2) {
			return 0;
		} else {
			eligibleChunksForSpawning.clear();
			int var4;
			int var7;

			for (var4 = 0; var4 < par0WorldServer.playerEntities.size(); ++var4) {
				EntityPlayerMP var5 = (EntityPlayerMP) par0WorldServer.playerEntities.get(var4);
				int var6 = MathHelper.floor_double(var5.posX / 16.0D);
				var7 = MathHelper.floor_double(var5.posZ / 16.0D);
				int var8 = 8;

				if(var5.renderDistance < var8) {
					var8 = var5.renderDistance;
					if(var8 < 4) {
						var8 = 4;
					} // TODO
				}
				
				for (int var9 = -var8; var9 <= var8; ++var9) {
					for (int var10 = -var8; var10 <= var8; ++var10) {
						boolean var11 = var8 >= 8 && (var9 == -var8 || var9 == var8 || var10 == -var8 || var10 == var8);
						ChunkCoordIntPair var12 = new ChunkCoordIntPair(var9 + var6, var10 + var7);

						if (!var11) {
							eligibleChunksForSpawning.put(var12, Boolean.valueOf(false));
						} else if (!eligibleChunksForSpawning.containsKey(var12)) {
							eligibleChunksForSpawning.put(var12, Boolean.valueOf(true));
						}
					}
				}
			}

			var4 = 0;
			ChunkCoordinates var32 = par0WorldServer.getSpawnPoint();
			EnumCreatureType[] var33 = EnumCreatureType.values();
			var7 = var33.length;

			for (int var34 = 0; var34 < var7; ++var34) {
				EnumCreatureType var35 = var33[var34];

				if ((!var35.getPeacefulCreature() || par2) && (var35.getPeacefulCreature() || par1)
						&& (!var35.getAnimal() || par3)
						&& par0WorldServer.countEntities(var35.getCreatureClass()) <= var35.getMaxNumberOfCreature()
								* eligibleChunksForSpawning.size() / 256) {
					Iterator var36 = eligibleChunksForSpawning.keySet().iterator();
					label110:

					while (var36.hasNext()) {
						ChunkCoordIntPair var37 = (ChunkCoordIntPair) var36.next();

						if (!((Boolean) eligibleChunksForSpawning.get(var37)).booleanValue()) {
							ChunkPosition var38 = getRandomSpawningPointInChunk(par0WorldServer, var37.chunkXPos,
									var37.chunkZPos);
							int var13 = var38.x;
							int var14 = var38.y;
							int var15 = var38.z;

							if (!par0WorldServer.isBlockNormalCube(var13, var14, var15) && par0WorldServer
									.getBlockMaterial(var13, var14, var15) == var35.getCreatureMaterial()) {
								int var16 = 0;
								int var17 = 0;

								while (var17 < 3) {
									int var18 = var13;
									int var19 = var14;
									int var20 = var15;
									byte var21 = 6;
									SpawnListEntry var22 = null;
									int var23 = 0;

									while (true) {
										if (var23 < 4) {
											label103: {
												var18 += par0WorldServer.rand.nextInt(var21)
														- par0WorldServer.rand.nextInt(var21);
												var19 += par0WorldServer.rand.nextInt(1)
														- par0WorldServer.rand.nextInt(1);
												var20 += par0WorldServer.rand.nextInt(var21)
														- par0WorldServer.rand.nextInt(var21);

												if (canCreatureTypeSpawnAtLocation(var35, par0WorldServer, var18, var19,
														var20)) {
													float var24 = (float) var18 + 0.5F;
													float var25 = (float) var19;
													float var26 = (float) var20 + 0.5F;

													if (par0WorldServer.getClosestPlayerForSpawning((double) var24,
															(double) var25, (double) var26) == null) {
														float var27 = var24 - (float) var32.posX;
														float var28 = var25 - (float) var32.posY;
														float var29 = var26 - (float) var32.posZ;
														float var30 = var27 * var27 + var28 * var28 + var29 * var29;

														if (var30 >= 576.0F) {
															if (var22 == null) {
																var22 = par0WorldServer.spawnRandomCreature(var35,
																		var18, var19, var20);

																if (var22 == null) {
																	break label103;
																}
															}

															EntityLiving var39;

															try {
																var39 = (EntityLiving) var22.entityConstructor.apply(par0WorldServer);
															} catch (Exception var31) {
																var31.printStackTrace();
																return var4;
															}

															var39.setLocationAndAngles((double) var24, (double) var25,
																	(double) var26,
																	par0WorldServer.rand.nextFloat() * 360.0F, 0.0F);

															if (var39.getCanSpawnHere()) {
																++var16;
																par0WorldServer.spawnEntityInWorld(var39);
																creatureSpecificInit(var39, par0WorldServer, var24,
																		var25, var26);

																if (var16 >= var39.getMaxSpawnedInChunk()) {
																	continue label110;
																}
															}

															var4 += var16;
														}
													}
												}

												++var23;
												continue;
											}
										}

										++var17;
										break;
									}
								}
							}
						}
					}
				}
			}

			return var4;
		}
	}

	/**
	 * Returns whether or not the specified creature type can spawn at the specified
	 * location.
	 */
	public static boolean canCreatureTypeSpawnAtLocation(EnumCreatureType par0EnumCreatureType, World par1World,
			int par2, int par3, int par4) {
		if (par0EnumCreatureType.getCreatureMaterial() == Material.water) {
			return par1World.getBlockMaterial(par2, par3, par4).isLiquid()
					&& par1World.getBlockMaterial(par2, par3 - 1, par4).isLiquid()
					&& !par1World.isBlockNormalCube(par2, par3 + 1, par4);
		} else if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4)) {
			return false;
		} else {
			int var5 = par1World.getBlockId(par2, par3 - 1, par4);
			return var5 != Block.bedrock.blockID && !par1World.isBlockNormalCube(par2, par3, par4)
					&& !par1World.getBlockMaterial(par2, par3, par4).isLiquid()
					&& !par1World.isBlockNormalCube(par2, par3 + 1, par4);
		}
	}

	/**
	 * determines if a skeleton spawns on a spider, and if a sheep is a different
	 * color
	 */
	private static void creatureSpecificInit(EntityLiving par0EntityLiving, World par1World, float par2, float par3,
			float par4) {
		par0EntityLiving.initCreature();
	}

	/**
	 * Called during chunk generation to spawn initial creatures.
	 */
	public static void performWorldGenSpawning(World par0World, BiomeGenBase par1BiomeGenBase, int par2, int par3,
			int par4, int par5, EaglercraftRandom par6Random) {
		List var7 = par1BiomeGenBase.getSpawnableList(EnumCreatureType.creature);

		if (!var7.isEmpty()) {
			while (par6Random.nextFloat() < par1BiomeGenBase.getSpawningChance()) {
				SpawnListEntry var8 = (SpawnListEntry) WeightedRandom.getRandomItem(par0World.rand, var7);
				int var9 = var8.minGroupCount + par6Random.nextInt(1 + var8.maxGroupCount - var8.minGroupCount);
				int var10 = par2 + par6Random.nextInt(par4);
				int var11 = par3 + par6Random.nextInt(par5);
				int var12 = var10;
				int var13 = var11;

				for (int var14 = 0; var14 < var9; ++var14) {
					boolean var15 = false;

					for (int var16 = 0; !var15 && var16 < 4; ++var16) {
						int var17 = par0World.getTopSolidOrLiquidBlock(var10, var11);

						if (canCreatureTypeSpawnAtLocation(EnumCreatureType.creature, par0World, var10, var17, var11)) {
							float var18 = (float) var10 + 0.5F;
							float var19 = (float) var17;
							float var20 = (float) var11 + 0.5F;
							EntityLiving var21;

							try {
								var21 = (EntityLiving) var8.entityConstructor.apply(par0World);
							} catch (Exception var23) {
								var23.printStackTrace();
								continue;
							}

							var21.setLocationAndAngles((double) var18, (double) var19, (double) var20,
									par6Random.nextFloat() * 360.0F, 0.0F);
							par0World.spawnEntityInWorld(var21);
							creatureSpecificInit(var21, par0World, var18, var19, var20);
							var15 = true;
						}

						var10 += par6Random.nextInt(5) - par6Random.nextInt(5);

						for (var11 += par6Random.nextInt(5) - par6Random.nextInt(5); var10 < par2
								|| var10 >= par2 + par4 || var11 < par3 || var11 >= par3 + par4; var11 = var13
										+ par6Random.nextInt(5) - par6Random.nextInt(5)) {
							var10 = var12 + par6Random.nextInt(5) - par6Random.nextInt(5);
						}
					}
				}
			}
		}
	}
}
