package net.minecraft.src;

public class EntityAIArrowAttack extends EntityAIBase {
	/** The entity the AI instance has been applied to */
	private final EntityLiving entityHost;

	/**
	 * The entity (as a RangedAttackMob) the AI instance has been applied to.
	 */
	private final IRangedAttackMob rangedAttackEntityHost;
	private EntityLiving attackTarget;

	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It
	 * is then set back to the maxRangedAttackTime.
	 */
	private int rangedAttackTime;
	private float entityMoveSpeed;
	private int field_75318_f;
	private int field_96561_g;

	/**
	 * The maximum time the AI has to wait before peforming another ranged attack.
	 */
	private int maxRangedAttackTime;
	private float field_96562_i;
	private float field_82642_h;

	public EntityAIArrowAttack(IRangedAttackMob par1IRangedAttackMob, float par2, int par3, float par4) {
		this(par1IRangedAttackMob, par2, par3, par3, par4);
	}

	public EntityAIArrowAttack(IRangedAttackMob par1IRangedAttackMob, float par2, int par3, int par4, float par5) {
		this.rangedAttackTime = -1;
		this.field_75318_f = 0;

		if (!(par1IRangedAttackMob instanceof EntityLiving)) {
			throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
		} else {
			this.rangedAttackEntityHost = par1IRangedAttackMob;
			this.entityHost = (EntityLiving) par1IRangedAttackMob;
			this.entityMoveSpeed = par2;
			this.field_96561_g = par3;
			this.maxRangedAttackTime = par4;
			this.field_96562_i = par5;
			this.field_82642_h = par5 * par5;
			this.setMutexBits(3);
		}
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		EntityLiving var1 = this.entityHost.getAttackTarget();

		if (var1 == null) {
			return false;
		} else {
			this.attackTarget = var1;
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		this.attackTarget = null;
		this.field_75318_f = 0;
		this.rangedAttackTime = -1;
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		double var1 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.boundingBox.minY,
				this.attackTarget.posZ);
		boolean var3 = this.entityHost.getEntitySenses().canSee(this.attackTarget);

		if (var3) {
			++this.field_75318_f;
		} else {
			this.field_75318_f = 0;
		}

		if (var1 <= (double) this.field_82642_h && this.field_75318_f >= 20) {
			this.entityHost.getNavigator().clearPathEntity();
		} else {
			this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
		}

		this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
		float var4;

		if (--this.rangedAttackTime == 0) {
			if (var1 > (double) this.field_82642_h || !var3) {
				return;
			}

			var4 = MathHelper.sqrt_double(var1) / this.field_96562_i;
			float var5 = var4;

			if (var4 < 0.1F) {
				var5 = 0.1F;
			}

			if (var5 > 1.0F) {
				var5 = 1.0F;
			}

			this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, var5);
			this.rangedAttackTime = MathHelper.floor_float(
					var4 * (float) (this.maxRangedAttackTime - this.field_96561_g) + (float) this.field_96561_g);
		} else if (this.rangedAttackTime < 0) {
			var4 = MathHelper.sqrt_double(var1) / this.field_96562_i;
			this.rangedAttackTime = MathHelper.floor_float(
					var4 * (float) (this.maxRangedAttackTime - this.field_96561_g) + (float) this.field_96561_g);
		}
	}
}
