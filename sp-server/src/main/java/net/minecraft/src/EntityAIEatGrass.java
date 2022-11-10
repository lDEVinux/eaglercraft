package net.minecraft.src;

public class EntityAIEatGrass extends EntityAIBase {
	private EntityLiving theEntity;
	private World theWorld;

	/** A decrementing tick used for the sheep's head offset and animation. */
	int eatGrassTick = 0;

	public EntityAIEatGrass(EntityLiving par1EntityLiving) {
		this.theEntity = par1EntityLiving;
		this.theWorld = par1EntityLiving.worldObj;
		this.setMutexBits(7);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.theEntity.getRNG().nextInt(this.theEntity.isChild() ? 50 : 1000) != 0) {
			return false;
		} else {
			int var1 = MathHelper.floor_double(this.theEntity.posX);
			int var2 = MathHelper.floor_double(this.theEntity.posY);
			int var3 = MathHelper.floor_double(this.theEntity.posZ);
			return this.theWorld.getBlockId(var1, var2, var3) == Block.tallGrass.blockID
					&& this.theWorld.getBlockMetadata(var1, var2, var3) == 1 ? true
							: this.theWorld.getBlockId(var1, var2 - 1, var3) == Block.grass.blockID;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.eatGrassTick = 40;
		this.theWorld.setEntityState(this.theEntity, (byte) 10);
		this.theEntity.getNavigator().clearPathEntity();
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		this.eatGrassTick = 0;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return this.eatGrassTick > 0;
	}

	public int getEatGrassTick() {
		return this.eatGrassTick;
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		this.eatGrassTick = Math.max(0, this.eatGrassTick - 1);

		if (this.eatGrassTick == 4) {
			int var1 = MathHelper.floor_double(this.theEntity.posX);
			int var2 = MathHelper.floor_double(this.theEntity.posY);
			int var3 = MathHelper.floor_double(this.theEntity.posZ);

			if (this.theWorld.getBlockId(var1, var2, var3) == Block.tallGrass.blockID) {
				this.theWorld.destroyBlock(var1, var2, var3, false);
				this.theEntity.eatGrassBonus();
			} else if (this.theWorld.getBlockId(var1, var2 - 1, var3) == Block.grass.blockID) {
				this.theWorld.playAuxSFX(2001, var1, var2 - 1, var3, Block.grass.blockID);
				this.theWorld.setBlock(var1, var2 - 1, var3, Block.dirt.blockID, 0, 2);
				this.theEntity.eatGrassBonus();
			}
		}
	}
}
