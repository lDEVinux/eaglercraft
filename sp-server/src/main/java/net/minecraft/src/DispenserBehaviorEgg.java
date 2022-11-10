package net.minecraft.src;

final class DispenserBehaviorEgg extends BehaviorProjectileDispense {
	/**
	 * Return the projectile entity spawned by this dispense behavior.
	 */
	protected IProjectile getProjectileEntity(World par1World, IPosition par2IPosition) {
		return new EntityEgg(par1World, par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ());
	}
}
