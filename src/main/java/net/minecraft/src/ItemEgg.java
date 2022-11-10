package net.minecraft.src;

public class ItemEgg extends Item {
	public ItemEgg(int par1) {
		super(par1);
		this.maxStackSize = 16;
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed.
	 * Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (!par3EntityPlayer.capabilities.isCreativeMode) {
			--par1ItemStack.stackSize;
		}

		par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		return par1ItemStack;
	}
}
