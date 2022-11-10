package net.minecraft.src;

public class EntityAIWatchClosest extends EntityAIBase {
	private EntityLiving theWatcher;

	/** The closest entity which is being watched by this one. */
	protected Entity closestEntity;
	private float field_75333_c;
	private int lookTime;
	private float field_75331_e;
	private Class watchedClass;

	public EntityAIWatchClosest(EntityLiving par1EntityLiving, Class par2Class, float par3) {
		this.theWatcher = par1EntityLiving;
		this.watchedClass = par2Class;
		this.field_75333_c = par3;
		this.field_75331_e = 0.02F;
		this.setMutexBits(2);
	}

	public EntityAIWatchClosest(EntityLiving par1EntityLiving, Class par2Class, float par3, float par4) {
		this.theWatcher = par1EntityLiving;
		this.watchedClass = par2Class;
		this.field_75333_c = par3;
		this.field_75331_e = par4;
		this.setMutexBits(2);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.theWatcher.getRNG().nextFloat() >= this.field_75331_e) {
			return false;
		} else {
			if (this.watchedClass == EntityPlayer.class) {
				this.closestEntity = this.theWatcher.worldObj.getClosestPlayerToEntity(this.theWatcher,
						(double) this.field_75333_c);
			} else {
				this.closestEntity = this.theWatcher.worldObj.findNearestEntityWithinAABB(this.watchedClass,
						this.theWatcher.boundingBox.expand((double) this.field_75333_c, 3.0D,
								(double) this.field_75333_c),
						this.theWatcher);
			}

			return this.closestEntity != null;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return !this.closestEntity.isEntityAlive() ? false
				: (this.theWatcher
						.getDistanceSqToEntity(this.closestEntity) > (double) (this.field_75333_c * this.field_75333_c)
								? false
								: this.lookTime > 0);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.lookTime = 40 + this.theWatcher.getRNG().nextInt(40);
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		this.closestEntity = null;
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		this.theWatcher.getLookHelper().setLookPosition(this.closestEntity.posX,
				this.closestEntity.posY + (double) this.closestEntity.getEyeHeight(), this.closestEntity.posZ, 10.0F,
				(float) this.theWatcher.getVerticalFaceSpeed());
		--this.lookTime;
	}
}
