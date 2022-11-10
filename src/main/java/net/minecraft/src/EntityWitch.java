package net.minecraft.src;

public class EntityWitch extends EntityMob implements IRangedAttackMob {
	/** List of items a witch should drop on death. */
	private static final int[] witchDrops = new int[] { Item.lightStoneDust.itemID, Item.sugar.itemID, Item.redstone.itemID, Item.spiderEye.itemID, Item.glassBottle.itemID, Item.gunpowder.itemID, Item.stick.itemID, Item.stick.itemID };

	/**
	 * Timer used as interval for a witch's attack, decremented every tick if
	 * aggressive and when reaches zero the witch will throw a potion at the target
	 * entity.
	 */
	private int witchAttackTimer = 0;

	public EntityWitch() {
		super();
		this.moveSpeed = 0.25F;
	}

	protected void entityInit() {
		super.entityInit();
		this.getDataWatcher().addObject(21, Byte.valueOf((byte) 0));
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.witch.idle";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.witch.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.witch.death";
	}

	/**
	 * Set whether this witch is aggressive at an entity.
	 */
	public void setAggressive(boolean par1) {
		this.getDataWatcher().updateObject(21, Byte.valueOf((byte) (par1 ? 1 : 0)));
	}

	/**
	 * Return whether this witch is aggressive at an entity.
	 */
	public boolean getAggressive() {
		return this.getDataWatcher().getWatchableObjectByte(21) == 1;
	}

	public int getMaxHealth() {
		return 26;
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	public boolean isAIEnabled() {
		return true;
	}

	public void handleHealthUpdate(byte par1) {
		if (par1 == 15) {
			for (int var2 = 0; var2 < this.rand.nextInt(35) + 10; ++var2) {
				this.worldObj.spawnParticle("witchMagic", this.posX + this.rand.nextGaussian() * 0.12999999523162842D, this.boundingBox.maxY + 0.5D + this.rand.nextGaussian() * 0.12999999523162842D,
						this.posZ + this.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D);
			}
		} else {
			super.handleHealthUpdate(par1);
		}
	}

	/**
	 * Reduces damage, depending on potions
	 */
	protected int applyPotionDamageCalculations(DamageSource par1DamageSource, int par2) {
		par2 = super.applyPotionDamageCalculations(par1DamageSource, par2);

		if (par1DamageSource.getEntity() == this) {
			par2 = 0;
		}

		if (par1DamageSource.isMagicDamage()) {
			par2 = (int) ((double) par2 * 0.15D);
		}

		return par2;
	}

	/**
	 * This method returns a value to be applied directly to entity speed, this
	 * factor is less than 1 when a slowdown potion effect is applied, more than 1
	 * when a haste potion effect is applied and 2 for fleeing entities.
	 */
	public float getSpeedModifier() {
		float var1 = super.getSpeedModifier();

		if (this.getAggressive()) {
			var1 *= 0.75F;
		}

		return var1;
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity has
	 * recently been hit by a player. @param par2 - Level of Looting used to kill
	 * this mob.
	 */
	protected void dropFewItems(boolean par1, int par2) {
		int var3 = this.rand.nextInt(3) + 1;

		for (int var4 = 0; var4 < var3; ++var4) {
			int var5 = this.rand.nextInt(3);
			int var6 = witchDrops[this.rand.nextInt(witchDrops.length)];

			if (par2 > 0) {
				var5 += this.rand.nextInt(par2 + 1);
			}

			for (int var7 = 0; var7 < var5; ++var7) {
				this.dropItem(var6, 1);
			}
		}
	}

	/**
	 * Attack the specified entity using a ranged attack.
	 */
	public void attackEntityWithRangedAttack(EntityLiving par1EntityLiving, float par2) {
		if (!this.getAggressive()) {
			EntityPotion var3 = new EntityPotion(this.worldObj, this, 32732);
			var3.rotationPitch -= -20.0F;
			double var4 = par1EntityLiving.posX + par1EntityLiving.motionX - this.posX;
			double var6 = par1EntityLiving.posY + (double) par1EntityLiving.getEyeHeight() - 1.100000023841858D - this.posY;
			double var8 = par1EntityLiving.posZ + par1EntityLiving.motionZ - this.posZ;
			float var10 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);

			if (var10 >= 8.0F && !par1EntityLiving.isPotionActive(Potion.moveSlowdown)) {
				var3.setPotionDamage(32698);
			} else if (par1EntityLiving.getHealth() >= 8 && !par1EntityLiving.isPotionActive(Potion.poison)) {
				var3.setPotionDamage(32660);
			} else if (var10 <= 3.0F && !par1EntityLiving.isPotionActive(Potion.weakness) && this.rand.nextFloat() < 0.25F) {
				var3.setPotionDamage(32696);
			}

			var3.setThrowableHeading(var4, var6 + (double) (var10 * 0.2F), var8, 0.75F, 8.0F);
			this.worldObj.spawnEntityInWorld(var3);
		}
	}
}
