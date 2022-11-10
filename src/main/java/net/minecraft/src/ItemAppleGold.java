package net.minecraft.src;

import java.util.List;

public class ItemAppleGold extends ItemFood {
	public ItemAppleGold(int par1, int par2, float par3, boolean par4) {
		super(par1, par2, par3, par4);
		this.setHasSubtypes(true);
	}

	public boolean hasEffect(ItemStack par1ItemStack) {
		return par1ItemStack.getItemDamage() > 0;
	}

	/**
	 * Return an item rarity from EnumRarity
	 */
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return par1ItemStack.getItemDamage() == 0 ? EnumRarity.rare : EnumRarity.epic;
	}

	protected void onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (par1ItemStack.getItemDamage() <= 0) {
			super.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
		}
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns
	 * 16 items)
	 */
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
	}
}
