package net.minecraft.src;

import java.util.List;

public class ItemSkull extends Item {
	private static final String[] skullTypes = new String[] { "skeleton", "wither", "zombie", "char", "creeper" };
	public static final String[] field_94587_a = new String[] { "skull_skeleton", "skull_wither", "skull_zombie", "skull_char", "skull_creeper" };
	private Icon[] field_94586_c;

	public ItemSkull(int par1) {
		super(par1);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		if (par7 == 0) {
			return false;
		} else if (!par3World.getBlockMaterial(par4, par5, par6).isSolid()) {
			return false;
		} else {
			if (par7 == 1) {
				++par5;
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

			if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)) {
				return false;
			} else if (!Block.skull.canPlaceBlockAt(par3World, par4, par5, par6)) {
				return false;
			} else {
				par3World.setBlock(par4, par5, par6, Block.skull.blockID, par7, 2);
				int var11 = 0;

				if (par7 == 1) {
					var11 = MathHelper.floor_double((double) (par2EntityPlayer.rotationYaw * 16.0F / 360.0F) + 0.5D) & 15;
				}

				TileEntity var12 = par3World.getBlockTileEntity(par4, par5, par6);

				if (var12 != null && var12 instanceof TileEntitySkull) {
					String var13 = "";

					if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("SkullOwner")) {
						var13 = par1ItemStack.getTagCompound().getString("SkullOwner");
					}

					((TileEntitySkull) var12).setSkullType(par1ItemStack.getItemDamage(), var13);
					((TileEntitySkull) var12).setSkullRotation(var11);
					((BlockSkull) Block.skull).makeWither(par3World, par4, par5, par6, (TileEntitySkull) var12);
				}

				--par1ItemStack.stackSize;
				return true;
			}
		}
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns
	 * 16 items)
	 */
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		for (int var4 = 0; var4 < skullTypes.length; ++var4) {
			par3List.add(new ItemStack(par1, 1, var4));
		}
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	public Icon getIconFromDamage(int par1) {
		if (par1 < 0 || par1 >= skullTypes.length) {
			par1 = 0;
		}

		return this.field_94586_c[par1];
	}

	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	public int getMetadata(int par1) {
		return par1;
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an ItemStack
	 * so different stacks can have different names based on their damage or NBT.
	 */
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		int var2 = par1ItemStack.getItemDamage();

		if (var2 < 0 || var2 >= skullTypes.length) {
			var2 = 0;
		}

		return super.getUnlocalizedName() + "." + skullTypes[var2];
	}

	public String getItemDisplayName(ItemStack par1ItemStack) {
		return par1ItemStack.getItemDamage() == 3 && par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("SkullOwner")
				? StatCollector.translateToLocalFormatted("item.skull.player.name", new Object[] { par1ItemStack.getTagCompound().getString("SkullOwner") })
				: super.getItemDisplayName(par1ItemStack);
	}

	public void registerIcons(IconRegister par1IconRegister) {
		this.field_94586_c = new Icon[field_94587_a.length];

		for (int var2 = 0; var2 < field_94587_a.length; ++var2) {
			this.field_94586_c[var2] = par1IconRegister.registerIcon(field_94587_a[var2]);
		}
	}
}
