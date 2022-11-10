package net.minecraft.src;

public class ItemMinecart extends Item {
	public int minecartType;

	public ItemMinecart(int par1, int par2) {
		super(par1);
		this.maxStackSize = 1;
		this.minecartType = par2;
		this.setCreativeTab(CreativeTabs.tabTransport);
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		int var11 = par3World.getBlockId(par4, par5, par6);

		if (BlockRailBase.isRailBlock(var11)) {
			--par1ItemStack.stackSize;
			return true;
		} else {
			return false;
		}
	}
}
