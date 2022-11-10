package net.minecraft.src;

public class ItemCarrotOnAStick extends Item {
	public ItemCarrotOnAStick(int par1) {
		super(par1);
		this.setCreativeTab(CreativeTabs.tabTransport);
		this.setMaxStackSize(1);
		this.setMaxDamage(25);
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed.
	 * Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (par3EntityPlayer.isRiding() && par3EntityPlayer.ridingEntity instanceof EntityPig) {
			EntityPig var4 = (EntityPig) par3EntityPlayer.ridingEntity;

			if (var4.getAIControlledByPlayer().isControlledByPlayer()
					&& par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage() >= 7) {
				var4.getAIControlledByPlayer().boostSpeed();
				par1ItemStack.damageItem(7, par3EntityPlayer);

				if (par1ItemStack.stackSize == 0) {
					ItemStack var5 = new ItemStack(Item.fishingRod);
					var5.setTagCompound(par1ItemStack.stackTagCompound);
					return var5;
				}
			}
		}

		return par1ItemStack;
	}
}
