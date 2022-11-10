package net.minecraft.src;

public class ItemEnderEye extends Item {
	public ItemEnderEye(int par1) {
		super(par1);
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4,
			int par5, int par6, int par7, float par8, float par9, float par10) {
		int var11 = par3World.getBlockId(par4, par5, par6);
		int var12 = par3World.getBlockMetadata(par4, par5, par6);

		if (par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)
				&& var11 == Block.endPortalFrame.blockID && !BlockEndPortalFrame.isEnderEyeInserted(var12)) {
			if (par3World.isRemote) {
				return true;
			} else {
				par3World.setBlockMetadata(par4, par5, par6, var12 + 4, 2);
				--par1ItemStack.stackSize;
				int var13;

				for (var13 = 0; var13 < 16; ++var13) {
					double var14 = (double) ((float) par4 + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F);
					double var16 = (double) ((float) par5 + 0.8125F);
					double var18 = (double) ((float) par6 + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F);
					double var20 = 0.0D;
					double var22 = 0.0D;
					double var24 = 0.0D;
					par3World.spawnParticle("smoke", var14, var16, var18, var20, var22, var24);
				}

				var13 = var12 & 3;
				int var26 = 0;
				int var15 = 0;
				boolean var27 = false;
				boolean var17 = true;
				int var28 = Direction.enderEyeMetaToDirection[var13];
				int var19;
				int var21;
				int var23;
				int var29;
				int var30;

				for (var19 = -2; var19 <= 2; ++var19) {
					var29 = par4 + Direction.offsetX[var28] * var19;
					var21 = par6 + Direction.offsetZ[var28] * var19;
					var30 = par3World.getBlockId(var29, par5, var21);

					if (var30 == Block.endPortalFrame.blockID) {
						var23 = par3World.getBlockMetadata(var29, par5, var21);

						if (!BlockEndPortalFrame.isEnderEyeInserted(var23)) {
							var17 = false;
							break;
						}

						var15 = var19;

						if (!var27) {
							var26 = var19;
							var27 = true;
						}
					}
				}

				if (var17 && var15 == var26 + 2) {
					for (var19 = var26; var19 <= var15; ++var19) {
						var29 = par4 + Direction.offsetX[var28] * var19;
						var21 = par6 + Direction.offsetZ[var28] * var19;
						var29 += Direction.offsetX[var13] * 4;
						var21 += Direction.offsetZ[var13] * 4;
						var30 = par3World.getBlockId(var29, par5, var21);
						var23 = par3World.getBlockMetadata(var29, par5, var21);

						if (var30 != Block.endPortalFrame.blockID || !BlockEndPortalFrame.isEnderEyeInserted(var23)) {
							var17 = false;
							break;
						}
					}

					for (var19 = var26 - 1; var19 <= var15 + 1; var19 += 4) {
						for (var29 = 1; var29 <= 3; ++var29) {
							var21 = par4 + Direction.offsetX[var28] * var19;
							var30 = par6 + Direction.offsetZ[var28] * var19;
							var21 += Direction.offsetX[var13] * var29;
							var30 += Direction.offsetZ[var13] * var29;
							var23 = par3World.getBlockId(var21, par5, var30);
							int var31 = par3World.getBlockMetadata(var21, par5, var30);

							if (var23 != Block.endPortalFrame.blockID
									|| !BlockEndPortalFrame.isEnderEyeInserted(var31)) {
								var17 = false;
								break;
							}
						}
					}

					if (var17) {
						for (var19 = var26; var19 <= var15; ++var19) {
							for (var29 = 1; var29 <= 3; ++var29) {
								var21 = par4 + Direction.offsetX[var28] * var19;
								var30 = par6 + Direction.offsetZ[var28] * var19;
								var21 += Direction.offsetX[var13] * var29;
								var30 += Direction.offsetZ[var13] * var29;
								par3World.setBlock(var21, par5, var30, Block.endPortal.blockID, 0, 2);
							}
						}
					}
				}

				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed.
	 * Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		MovingObjectPosition var4 = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, false);

		if (var4 != null && var4.typeOfHit == EnumMovingObjectType.TILE) {
			int var5 = par2World.getBlockId(var4.blockX, var4.blockY, var4.blockZ);

			if (var5 == Block.endPortalFrame.blockID) {
				return par1ItemStack;
			}
		}

		if (!par2World.isRemote) {
			ChunkPosition var7 = par2World.findClosestStructure("Stronghold", (int) par3EntityPlayer.posX,
					(int) par3EntityPlayer.posY, (int) par3EntityPlayer.posZ);

			if (var7 != null) {
				EntityEnderEye var6 = new EntityEnderEye(par2World, par3EntityPlayer.posX,
						par3EntityPlayer.posY + 1.62D - (double) par3EntityPlayer.yOffset, par3EntityPlayer.posZ);
				var6.moveTowards((double) var7.x, var7.y, (double) var7.z);
				par2World.spawnEntityInWorld(var6);
				par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F,
						0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
				par2World.playAuxSFXAtEntity((EntityPlayer) null, 1002, (int) par3EntityPlayer.posX,
						(int) par3EntityPlayer.posY, (int) par3EntityPlayer.posZ, 0);

				if (!par3EntityPlayer.capabilities.isCreativeMode) {
					--par1ItemStack.stackSize;
				}
			}
		}

		return par1ItemStack;
	}
}
