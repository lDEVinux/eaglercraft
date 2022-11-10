package net.minecraft.src;

public class ItemMap extends ItemMapBase {
	protected ItemMap(int par1) {
		super(par1);
		this.setHasSubtypes(true);
	}

	public MapData getMapData(ItemStack par1ItemStack, World par2World) {
		String var3 = "map_" + par1ItemStack.getItemDamage();
		MapData var4 = (MapData) par2World.loadItemData((s) -> new MapData(s), var3);

		if (var4 == null && !par2World.isRemote) {
			par1ItemStack.setItemDamage(par2World.getUniqueDataId("map"));
			var3 = "map_" + par1ItemStack.getItemDamage();
			var4 = new MapData(var3);
			var4.scale = 3;
			int var5 = 128 * (1 << var4.scale);
			var4.xCenter = Math.round((float) par2World.getWorldInfo().getSpawnX() / (float) var5) * var5;
			var4.zCenter = Math.round((float) (par2World.getWorldInfo().getSpawnZ() / var5)) * var5;
			var4.dimension = (byte) par2World.provider.dimensionId;
			var4.markDirty();
			par2World.setItemData(var3, var4);
		}

		return var4;
	}

	public void updateMapData(World par1World, Entity par2Entity, MapData par3MapData) {
		if (par1World.provider.dimensionId == par3MapData.dimension && par2Entity instanceof EntityPlayer) {
			short var4 = 128;
			short var5 = 128;
			int var6 = 1 << par3MapData.scale;
			int var7 = par3MapData.xCenter;
			int var8 = par3MapData.zCenter;
			int var9 = MathHelper.floor_double(par2Entity.posX - (double) var7) / var6 + var4 / 2;
			int var10 = MathHelper.floor_double(par2Entity.posZ - (double) var8) / var6 + var5 / 2;
			int var11 = 128 / var6;

			if (par1World.provider.hasNoSky) {
				var11 /= 2;
			}

			MapInfo var12 = par3MapData.func_82568_a((EntityPlayer) par2Entity);
			++var12.field_82569_d;

			for (int var13 = var9 - var11 + 1; var13 < var9 + var11; ++var13) {
				if ((var13 & 15) == (var12.field_82569_d & 15)) {
					int var14 = 255;
					int var15 = 0;
					double var16 = 0.0D;

					for (int var18 = var10 - var11 - 1; var18 < var10 + var11; ++var18) {
						if (var13 >= 0 && var18 >= -1 && var13 < var4 && var18 < var5) {
							int var19 = var13 - var9;
							int var20 = var18 - var10;
							boolean var21 = var19 * var19 + var20 * var20 > (var11 - 2) * (var11 - 2);
							int var22 = (var7 / var6 + var13 - var4 / 2) * var6;
							int var23 = (var8 / var6 + var18 - var5 / 2) * var6;
							int[] var24 = new int[256];
							Chunk var25 = par1World.getChunkFromBlockCoords(var22, var23);

							if (!var25.isEmpty()) {
								int var26 = var22 & 15;
								int var27 = var23 & 15;
								int var28 = 0;
								double var29 = 0.0D;
								int var31;
								int var32;
								int var33;
								int var36;

								if (par1World.provider.hasNoSky) {
									var31 = var22 + var23 * 231871;
									var31 = var31 * var31 * 31287121 + var31 * 11;

									if ((var31 >> 20 & 1) == 0) {
										var24[Block.dirt.blockID] += 10;
									} else {
										var24[Block.stone.blockID] += 10;
									}

									var29 = 100.0D;
								} else {
									for (var31 = 0; var31 < var6; ++var31) {
										for (var32 = 0; var32 < var6; ++var32) {
											var33 = var25.getHeightValue(var31 + var26, var32 + var27) + 1;
											int var34 = 0;

											if (var33 > 1) {
												boolean var35;

												do {
													var35 = true;
													var34 = var25.getBlockID(var31 + var26, var33 - 1, var32 + var27);

													if (var34 == 0) {
														var35 = false;
													} else if (var33 > 0 && var34 > 0
															&& Block.blocksList[var34].blockMaterial.materialMapColor == MapColor.airColor) {
														var35 = false;
													}

													if (!var35) {
														--var33;

														if (var33 <= 0) {
															break;
														}

														var34 = var25.getBlockID(var31 + var26, var33 - 1,
																var32 + var27);
													}
												} while (var33 > 0 && !var35);

												if (var33 > 0 && var34 != 0
														&& Block.blocksList[var34].blockMaterial.isLiquid()) {
													var36 = var33 - 1;
													boolean var37 = false;
													int var41;

													do {
														var41 = var25.getBlockID(var31 + var26, var36--, var32 + var27);
														++var28;
													} while (var36 > 0 && var41 != 0
															&& Block.blocksList[var41].blockMaterial.isLiquid());
												}
											}

											var29 += (double) var33 / (double) (var6 * var6);
											++var24[var34];
										}
									}
								}

								var28 /= var6 * var6;
								var31 = 0;
								var32 = 0;

								for (var33 = 0; var33 < 256; ++var33) {
									if (var24[var33] > var31) {
										var32 = var33;
										var31 = var24[var33];
									}
								}

								double var39 = (var29 - var16) * 4.0D / (double) (var6 + 4)
										+ ((double) (var13 + var18 & 1) - 0.5D) * 0.4D;
								byte var40 = 1;

								if (var39 > 0.6D) {
									var40 = 2;
								}

								if (var39 < -0.6D) {
									var40 = 0;
								}

								var36 = 0;

								if (var32 > 0) {
									MapColor var42 = Block.blocksList[var32].blockMaterial.materialMapColor;

									if (var42 == MapColor.waterColor) {
										var39 = (double) var28 * 0.1D + (double) (var13 + var18 & 1) * 0.2D;
										var40 = 1;

										if (var39 < 0.5D) {
											var40 = 2;
										}

										if (var39 > 0.9D) {
											var40 = 0;
										}
									}

									var36 = var42.colorIndex;
								}

								var16 = var29;

								if (var18 >= 0 && var19 * var19 + var20 * var20 < var11 * var11
										&& (!var21 || (var13 + var18 & 1) != 0)) {
									byte var43 = par3MapData.colors[var13 + var18 * var4];
									byte var38 = (byte) (var36 * 4 + var40);

									if (var43 != var38) {
										if (var14 > var18) {
											var14 = var18;
										}

										if (var15 < var18) {
											var15 = var18;
										}

										par3MapData.colors[var13 + var18 * var4] = var38;
									}
								}
							}
						}
					}

					if (var14 <= var15) {
						par3MapData.setColumnDirty(var13, var14, var15);
					}
				}
			}
		}
	}

	/**
	 * Called each tick as long the item is on a player inventory. Uses by maps to
	 * check if is on a player hand and update it's contents.
	 */
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		if (!par2World.isRemote) {
			MapData var6 = this.getMapData(par1ItemStack, par2World);

			if (par3Entity instanceof EntityPlayer) {
				EntityPlayer var7 = (EntityPlayer) par3Entity;
				var6.updateVisiblePlayers(var7, par1ItemStack);
			}

			if (par5) {
				this.updateMapData(par2World, par3Entity, var6);
			}
		}
	}

	public Packet getUpdatePacket(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		byte[] var4 = this.getMapData(par1ItemStack, par2World).getUpdatePacketData(par1ItemStack, par2World,
				par3EntityPlayer);
		return var4 == null ? null
				: new Packet131MapData((short) Item.map.itemID, (short) par1ItemStack.getItemDamage(), var4);
	}

	/**
	 * Called when item is crafted/smelted. Used only by maps so far.
	 */
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().getBoolean("map_is_scaling")) {
			MapData var4 = Item.map.getMapData(par1ItemStack, par2World);
			par1ItemStack.setItemDamage(par2World.getUniqueDataId("map"));
			MapData var5 = new MapData("map_" + par1ItemStack.getItemDamage());
			var5.scale = (byte) (var4.scale + 1);

			if (var5.scale > 4) {
				var5.scale = 4;
			}

			var5.xCenter = var4.xCenter;
			var5.zCenter = var4.zCenter;
			var5.dimension = var4.dimension;
			var5.markDirty();
			par2World.setItemData("map_" + par1ItemStack.getItemDamage(), var5);
		}
	}
}
