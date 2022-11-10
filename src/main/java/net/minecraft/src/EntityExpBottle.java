package net.minecraft.src;



public class EntityExpBottle extends EntityThrowable {
	public EntityExpBottle() {
		super();
	}

	public EntityExpBottle(World par1World, EntityLiving par2EntityLiving) {
		super(par1World, par2EntityLiving);
	}

	public EntityExpBottle(World par1World, double par2, double par4, double par6) {
		super(par1World, par2, par4, par6);
	}

	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	protected float getGravityVelocity() {
		return 0.07F;
	}

	protected float func_70182_d() {
		return 0.7F;
	}

	protected float func_70183_g() {
		return -20.0F;
	}

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	protected void onImpact(MovingObjectPosition par1MovingObjectPosition) {
	}
}
