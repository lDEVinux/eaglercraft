package net.minecraft.src;

import java.util.List;

final class CreativeTabTools extends CreativeTabs {
	CreativeTabTools(int par1, String par2Str) {
		super(par1, par2Str);
	}

	/**
	 * the itemID for the item to be displayed on the tab
	 */
	public int getTabIconItemIndex() {
		return Item.axeIron.itemID;
	}

	/**
	 * only shows items which have tabToDisplayOn == this
	 */
	public void displayAllReleventItems(List par1List) {
		super.displayAllReleventItems(par1List);
		this.func_92116_a(par1List, new EnumEnchantmentType[] { EnumEnchantmentType.digger });
	}
}
