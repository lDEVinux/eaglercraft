package net.minecraft.src;

public class ItemAppleGold extends ItemFood {
	public ItemAppleGold(int par1, int par2, float par3, boolean par4) {
		super(par1, par2, par3, par4);
		this.setHasSubtypes(true);
	}

	protected void onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (par1ItemStack.getItemDamage() > 0) {
			if (!par2World.isRemote) {
				par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.regeneration.id, 600, 3));
				par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.resistance.id, 6000, 0));
				par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 6000, 0));
			}
		} else {
			super.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
		}
	}
}
