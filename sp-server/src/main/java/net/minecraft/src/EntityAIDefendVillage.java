package net.minecraft.src;

public class EntityAIDefendVillage extends EntityAITarget {
	EntityIronGolem irongolem;

	/**
	 * The aggressor of the iron golem's village which is now the golem's attack
	 * target.
	 */
	EntityLiving villageAgressorTarget;

	public EntityAIDefendVillage(EntityIronGolem par1EntityIronGolem) {
		super(par1EntityIronGolem, 16.0F, false, true);
		this.irongolem = par1EntityIronGolem;
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		Village var1 = this.irongolem.getVillage();

		if (var1 == null) {
			return false;
		} else {
			this.villageAgressorTarget = var1.findNearestVillageAggressor(this.irongolem);

			if (!this.isSuitableTarget(this.villageAgressorTarget, false)) {
				if (this.taskOwner.getRNG().nextInt(20) == 0) {
					this.villageAgressorTarget = var1.func_82685_c(this.irongolem);
					return this.isSuitableTarget(this.villageAgressorTarget, false);
				} else {
					return false;
				}
			} else {
				return true;
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.irongolem.setAttackTarget(this.villageAgressorTarget);
		super.startExecuting();
	}
}
