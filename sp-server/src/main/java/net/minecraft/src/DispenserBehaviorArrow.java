package net.minecraft.src;

final class DispenserBehaviorArrow extends BehaviorProjectileDispense {
	/**
	 * Return the projectile entity spawned by this dispense behavior.
	 */
	protected IProjectile getProjectileEntity(World par1World, IPosition par2IPosition) {
		EntityArrow var3 = new EntityArrow(par1World, par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ());
		var3.canBePickedUp = 1;
		return var3;
	}
}
