package net.minecraft.src;

class DispenserBehaviorPotionProjectile extends BehaviorProjectileDispense {
	final ItemStack potionItemStack;

	final DispenserBehaviorPotion dispenserPotionBehavior;

	DispenserBehaviorPotionProjectile(DispenserBehaviorPotion par1DispenserBehaviorPotion, ItemStack par2ItemStack) {
		this.dispenserPotionBehavior = par1DispenserBehaviorPotion;
		this.potionItemStack = par2ItemStack;
	}

	/**
	 * Return the projectile entity spawned by this dispense behavior.
	 */
	protected IProjectile getProjectileEntity(World par1World, IPosition par2IPosition) {
		return new EntityPotion(par1World, par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ(),
				this.potionItemStack.copy());
	}

	protected float func_82498_a() {
		return super.func_82498_a() * 0.5F;
	}

	protected float func_82500_b() {
		return super.func_82500_b() * 1.25F;
	}
}
