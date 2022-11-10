package net.minecraft.src;

public class ItemFishingRod extends Item {
	private Icon theIcon;

	public ItemFishingRod(int par1) {
		super(par1);
		this.setMaxDamage(64);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.tabTools);
	}

	/**
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	public boolean isFull3D() {
		return true;
	}

	/**
	 * Returns true if this item should be rotated by 180 degrees around the Y axis
	 * when being held in an entities hands.
	 */
	public boolean shouldRotateAroundWhenRendering() {
		return true;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed.
	 * Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (par3EntityPlayer.fishEntity != null) {
			int var4 = par3EntityPlayer.fishEntity.catchFish();
			par1ItemStack.damageItem(var4, par3EntityPlayer);
			par3EntityPlayer.swingItem();
		} else {
			par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

			par3EntityPlayer.swingItem();
		}

		return par1ItemStack;
	}

	public void registerIcons(IconRegister par1IconRegister) {
		super.registerIcons(par1IconRegister);
		this.theIcon = par1IconRegister.registerIcon("fishingRod_empty");
	}

	public Icon func_94597_g() {
		return this.theIcon;
	}
}
