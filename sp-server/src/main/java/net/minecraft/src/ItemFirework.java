package net.minecraft.src;

public class ItemFirework extends Item {
	public ItemFirework(int par1) {
		super(par1);
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4,
			int par5, int par6, int par7, float par8, float par9, float par10) {
		if (!par3World.isRemote) {
			EntityFireworkRocket var11 = new EntityFireworkRocket(par3World, (double) ((float) par4 + par8),
					(double) ((float) par5 + par9), (double) ((float) par6 + par10), par1ItemStack);
			par3World.spawnEntityInWorld(var11);

			if (!par2EntityPlayer.capabilities.isCreativeMode) {
				--par1ItemStack.stackSize;
			}

			return true;
		} else {
			return false;
		}
	}
}
