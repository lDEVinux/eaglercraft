package net.minecraft.src;

import java.util.List;

public class ItemDye extends Item {
	/** List of dye color names */
	public static final String[] dyeColorNames = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "lightBlue", "magenta", "orange", "white" };
	public static final String[] field_94595_b = new String[] { "dyePowder_black", "dyePowder_red", "dyePowder_green", "dyePowder_brown", "dyePowder_blue", "dyePowder_purple", "dyePowder_cyan", "dyePowder_silver", "dyePowder_gray",
			"dyePowder_pink", "dyePowder_lime", "dyePowder_yellow", "dyePowder_lightBlue", "dyePowder_magenta", "dyePowder_orange", "dyePowder_white" };
	public static final int[] dyeColors = new int[] { 1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320 };
	private Icon[] field_94594_d;

	public ItemDye(int par1) {
		super(par1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	public Icon getIconFromDamage(int par1) {
		int var2 = MathHelper.clamp_int(par1, 0, 15);
		return this.field_94594_d[var2];
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an ItemStack
	 * so different stacks can have different names based on their damage or NBT.
	 */
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		int var2 = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, 15);
		return super.getUnlocalizedName() + "." + dyeColorNames[var2];
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)) {
			return false;
		} else {
			if (par1ItemStack.getItemDamage() == 15) {
				if (func_96604_a(par1ItemStack, par3World, par4, par5, par6)) {
					return true;
				}
			} else if (par1ItemStack.getItemDamage() == 3) {
				int var11 = par3World.getBlockId(par4, par5, par6);
				int var12 = par3World.getBlockMetadata(par4, par5, par6);

				if (var11 == Block.wood.blockID && BlockLog.limitToValidMetadata(var12) == 3) {
					if (par7 == 0) {
						return false;
					}

					if (par7 == 1) {
						return false;
					}

					if (par7 == 2) {
						--par6;
					}

					if (par7 == 3) {
						++par6;
					}

					if (par7 == 4) {
						--par4;
					}

					if (par7 == 5) {
						++par4;
					}

					if (par3World.isAirBlock(par4, par5, par6)) {
						int var13 = Block.blocksList[Block.cocoaPlant.blockID].onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, 0);
						par3World.setBlock(par4, par5, par6, Block.cocoaPlant.blockID, var13, 2);

						if (!par2EntityPlayer.capabilities.isCreativeMode) {
							--par1ItemStack.stackSize;
						}
					}

					return true;
				}
			}

			return false;
		}
	}

	public static boolean func_96604_a(ItemStack par0ItemStack, World par1World, int par2, int par3, int par4) {
		int var5 = par1World.getBlockId(par2, par3, par4);

		if (var5 == Block.sapling.blockID) {
			return true;
		} else if (var5 != Block.mushroomBrown.blockID && var5 != Block.mushroomRed.blockID) {
			if (var5 != Block.melonStem.blockID && var5 != Block.pumpkinStem.blockID) {
				if (var5 > 0 && Block.blocksList[var5] instanceof BlockCrops) {
					if (par1World.getBlockMetadata(par2, par3, par4) == 7) {
						return false;
					} else {
						return true;
					}
				} else {
					int var6;
					int var7;
					int var8;

					if (var5 == Block.cocoaPlant.blockID) {
						var6 = par1World.getBlockMetadata(par2, par3, par4);
						var7 = BlockDirectional.getDirection(var6);
						var8 = BlockCocoa.func_72219_c(var6);

						if (var8 >= 2) {
							return false;
						} else {
							return true;
						}
					} else if (var5 != Block.grass.blockID) {
						return false;
					} else {
						return true;
					}
				}
			} else if (par1World.getBlockMetadata(par2, par3, par4) == 7) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	public static void func_96603_a(World par0World, int par1, int par2, int par3, int par4) {
		int var5 = par0World.getBlockId(par1, par2, par3);

		if (par4 == 0) {
			par4 = 15;
		}

		Block var6 = var5 > 0 && var5 < Block.blocksList.length ? Block.blocksList[var5] : null;

		if (var6 != null) {
			var6.setBlockBoundsBasedOnState(par0World, par1, par2, par3);

			for (int var7 = 0; var7 < par4; ++var7) {
				double var8 = itemRand.nextGaussian() * 0.02D;
				double var10 = itemRand.nextGaussian() * 0.02D;
				double var12 = itemRand.nextGaussian() * 0.02D;
				par0World.spawnParticle("happyVillager", (double) ((float) par1 + itemRand.nextFloat()), (double) par2 + (double) itemRand.nextFloat() * var6.getBlockBoundsMaxY(), (double) ((float) par3 + itemRand.nextFloat()), var8, var10,
						var12);
			}
		}
	}

	/**
	 * Called when a player right clicks an entity with an item.
	 */
	public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityLiving par2EntityLiving) {
		if (par2EntityLiving instanceof EntitySheep) {
			EntitySheep var3 = (EntitySheep) par2EntityLiving;
			int var4 = BlockCloth.getBlockFromDye(par1ItemStack.getItemDamage());

			if (!var3.getSheared() && var3.getFleeceColor() != var4) {
				var3.setFleeceColor(var4);
				--par1ItemStack.stackSize;
			}

			return true;
		} else {
			return false;
		}
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns
	 * 16 items)
	 */
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		for (int var4 = 0; var4 < 16; ++var4) {
			par3List.add(new ItemStack(par1, 1, var4));
		}
	}

	public void registerIcons(IconRegister par1IconRegister) {
		this.field_94594_d = new Icon[field_94595_b.length];

		for (int var2 = 0; var2 < field_94595_b.length; ++var2) {
			this.field_94594_d[var2] = par1IconRegister.registerIcon(field_94595_b[var2]);
		}
	}
}
