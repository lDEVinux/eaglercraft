package net.minecraft.src;

public class ItemSaddle extends Item {
	public ItemSaddle(int par1) {
		super(par1);
		this.maxStackSize = 1;
		this.setCreativeTab(CreativeTabs.tabTransport);
	}

	/**
	 * Called when a player right clicks an entity with an item.
	 */
	public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityLiving par2EntityLiving) {
		if (par2EntityLiving instanceof EntityPig) {
			EntityPig var3 = (EntityPig) par2EntityLiving;

			if (!var3.getSaddled() && !var3.isChild()) {
				var3.setSaddled(true);
				--par1ItemStack.stackSize;
			}

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Current implementations of this method in child classes do not use the entry
	 * argument beside ev. They just raise the damage on the stack.
	 */
	public boolean hitEntity(ItemStack par1ItemStack, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving) {
		this.itemInteractionForEntity(par1ItemStack, par2EntityLiving);
		return true;
	}
}
