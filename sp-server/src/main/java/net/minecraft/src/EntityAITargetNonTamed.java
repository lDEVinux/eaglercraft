package net.minecraft.src;

public class EntityAITargetNonTamed extends EntityAINearestAttackableTarget {
	private EntityTameable theTameable;

	public EntityAITargetNonTamed(EntityTameable par1EntityTameable, Class par2Class, float par3, int par4,
			boolean par5) {
		super(par1EntityTameable, par2Class, par3, par4, par5);
		this.theTameable = par1EntityTameable;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		return this.theTameable.isTamed() ? false : super.shouldExecute();
	}
}
