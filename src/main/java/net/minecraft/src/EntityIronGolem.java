package net.minecraft.src;

public class EntityIronGolem extends EntityGolem {
	/** deincrements, and a distance-to-home check is done at 0 */
	private int homeCheckTimer = 0;
	private int attackTimer;
	private int holdRoseTick;

	public EntityIronGolem() {
		super();
		this.setSize(1.4F, 2.9F);
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	public boolean isAIEnabled() {
		return true;
	}

	/**
	 * main AI tick function, replaces updateEntityActionState
	 */
	protected void updateAITick() {

		super.updateAITick();
	}

	public int getMaxHealth() {
		return 100;
	}

	/**
	 * Decrements the entity's air supply when underwater
	 */
	protected int decreaseAirSupply(int par1) {
		return par1;
	}

	protected void collideWithEntity(Entity par1Entity) {
		if (par1Entity instanceof IMob && this.getRNG().nextInt(20) == 0) {
			this.setAttackTarget((EntityLiving) par1Entity);
		}

		super.collideWithEntity(par1Entity);
	}

	/**
	 * Called frequently so the entity can update its state every tick as required.
	 * For example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (this.attackTimer > 0) {
			--this.attackTimer;
		}

		if (this.holdRoseTick > 0) {
			--this.holdRoseTick;
		}

		if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.500000277905201E-7D && this.rand.nextInt(5) == 0) {
			int var1 = MathHelper.floor_double(this.posX);
			int var2 = MathHelper.floor_double(this.posY - 0.20000000298023224D - (double) this.yOffset);
			int var3 = MathHelper.floor_double(this.posZ);
			int var4 = this.worldObj.getBlockId(var1, var2, var3);

			if (var4 > 0) {
				this.worldObj.spawnParticle("tilecrack_" + var4 + "_" + this.worldObj.getBlockMetadata(var1, var2, var3), this.posX + ((double) this.rand.nextFloat() - 0.5D) * (double) this.width, this.boundingBox.minY + 0.1D,
						this.posZ + ((double) this.rand.nextFloat() - 0.5D) * (double) this.width, 4.0D * ((double) this.rand.nextFloat() - 0.5D), 0.5D, ((double) this.rand.nextFloat() - 0.5D) * 4.0D);
			}
		}
	}

	/**
	 * Returns true if this entity can attack entities of the specified class.
	 */
	public boolean canAttackClass(Class par1Class) {
		return this.isPlayerCreated() && EntityPlayer.class.isAssignableFrom(par1Class) ? false : super.canAttackClass(par1Class);
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("PlayerCreated", this.isPlayerCreated());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setPlayerCreated(par1NBTTagCompound.getBoolean("PlayerCreated"));
	}

	public boolean attackEntityAsMob(Entity par1Entity) {
		this.attackTimer = 10;
		this.worldObj.setEntityState(this, (byte) 4);
		boolean var2 = par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), 7 + this.rand.nextInt(15));

		if (var2) {
			par1Entity.motionY += 0.4000000059604645D;
		}

		this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
		return var2;
	}

	public void handleHealthUpdate(byte par1) {
		if (par1 == 4) {
			this.attackTimer = 10;
			this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
		} else if (par1 == 11) {
			this.holdRoseTick = 400;
		} else {
			super.handleHealthUpdate(par1);
		}
	}

	public int getAttackTimer() {
		return this.attackTimer;
	}

	public void setHoldingRose(boolean par1) {
		this.holdRoseTick = par1 ? 400 : 0;
		this.worldObj.setEntityState(this, (byte) 11);
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "none";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.irongolem.hit";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.irongolem.death";
	}

	/**
	 * Plays step sound at given x, y, z for the entity
	 */
	protected void playStepSound(int par1, int par2, int par3, int par4) {
		this.playSound("mob.irongolem.walk", 1.0F, 1.0F);
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity has
	 * recently been hit by a player. @param par2 - Level of Looting used to kill
	 * this mob.
	 */
	protected void dropFewItems(boolean par1, int par2) {
		int var3 = this.rand.nextInt(3);
		int var4;

		for (var4 = 0; var4 < var3; ++var4) {
			this.dropItem(Block.plantRed.blockID, 1);
		}

		var4 = 3 + this.rand.nextInt(3);

		for (int var5 = 0; var5 < var4; ++var5) {
			this.dropItem(Item.ingotIron.itemID, 1);
		}
	}

	public int getHoldRoseTick() {
		return this.holdRoseTick;
	}

	public boolean isPlayerCreated() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void setPlayerCreated(boolean par1) {
		byte var2 = this.dataWatcher.getWatchableObjectByte(16);

		if (par1) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 | 1)));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 & -2)));
		}
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	public void onDeath(DamageSource par1DamageSource) {

		super.onDeath(par1DamageSource);
	}
}
