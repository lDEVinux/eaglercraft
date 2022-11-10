package net.minecraft.src;

public class EntitySmallFireball extends EntityFireball {
	public EntitySmallFireball() {
		super();
		this.setSize(0.3125F, 0.3125F);
	}

	public EntitySmallFireball(World par1World, EntityLiving par2EntityLiving, double par3, double par5, double par7) {
		super(par1World, par2EntityLiving, par3, par5, par7);
		this.setSize(0.3125F, 0.3125F);
	}

	public EntitySmallFireball(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
		super(par1World, par2, par4, par6, par8, par10, par12);
		this.setSize(0.3125F, 0.3125F);
	}

	/**
	 * Called when this EntityFireball hits a block or entity.
	 */
	protected void onImpact(MovingObjectPosition par1MovingObjectPosition) {
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this
	 * Entity.
	 */
	public boolean canBeCollidedWith() {
		return false;
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		return false;
	}
}
