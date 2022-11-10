package net.minecraft.src;

public class ItemFireball extends Item {
	public ItemFireball(int par1) {
		super(par1);
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		return true;
	}
}
